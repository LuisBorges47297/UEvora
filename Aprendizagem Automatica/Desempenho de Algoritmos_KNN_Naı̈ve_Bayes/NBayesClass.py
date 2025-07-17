import numpy as np
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler

class NBayesClasseUE:
    def __init__(self, suave=1e-9):
        self.suave = suave
        self.classes = None
        self.medias = None
        self.variancias = None
        self.priors = None

    def fit(self, X, y):
        X = np.array(X, dtype=object)
        y = np.array(y)
        self.classes = np.unique(y)

        self.medias = {}
        self.variancias = {}
        self.priors = {}

        for classe in self.classes:
            X_classe = X[y == classe]
            num_features = X.shape[1]
            
            medias = []
            variancias = []

            for i in range(num_features):
                coluna = X_classe[:, i]
                if isinstance(coluna[0], str):  # Para dados categóricos
                    # Representação das categorias usando contagens
                    categorias, contagens = np.unique(coluna, return_counts=True)
                    prob_categorias = contagens / len(coluna)
                    medias.append(dict(zip(categorias, prob_categorias)))  # Probabilidades das categorias
                    variancias.append(None)  # Não é aplicável para dados categóricos
                else:
                    # Para dados numéricos
                    medias.append(np.mean(coluna))
                    variancias.append(np.var(coluna) + self.suave)

            self.medias[classe] = medias
            self.variancias[classe] = variancias
            self.priors[classe] = len(X_classe) / len(X)

    def _calcular_verossimilhanca(self, instancia, classe):
        verossimilhanca = 0
        medias = self.medias[classe]
        variancias = self.variancias[classe]

        for i in range(len(instancia)):
            atributo = instancia[i]
            media = medias[i]
            variancia = variancias[i]

            if isinstance(media, dict):  # Dados categóricos
                prob_categoria = media.get(atributo, 1e-9)  # Probabilidade da categoria
                verossimilhanca += np.log(prob_categoria)
            else:  # Dados numéricos
                coef = -0.5 * np.log(2 * np.pi * variancia)
                expoente = -0.5 * ((atributo - media) ** 2) / variancia
                verossimilhanca += coef + expoente

        return verossimilhanca

    def predict(self, X):
        previsoes = []

        for instancia in X:
            melhor_probabilidade = -np.inf
            classe_predita = None

            for classe in self.classes:
                log_prior = np.log(self.priors[classe])
                log_verossimilhanca = self._calcular_verossimilhanca(instancia, classe)
                log_posterior = log_prior + log_verossimilhanca

                if log_posterior > melhor_probabilidade:
                    melhor_probabilidade = log_posterior
                    classe_predita = classe

            previsoes.append(classe_predita)

        return np.array(previsoes)

    def score(self, X, y):
        y_pred = self.predict(X)
        return np.mean(y_pred == y)

# Main
datasets = ["iris.csv", "rice.csv", "wdbc.csv", "tic-tac-toe.csv"]  # Adicione o caminho do conjunto de dados adicional aqui
resultados = []

for dataset in datasets:
    ficheiro = pd.read_csv(dataset)
    X = ficheiro.iloc[:, 0:-1].values
    y = ficheiro.iloc[:, -1].values

    # Normalização apenas para atributos numéricos
    scaler = StandardScaler()
    for i in range(X.shape[1]):
        if not isinstance(X[0, i], str):
            X[:, i] = scaler.fit_transform(X[:, i].reshape(-1, 1)).flatten()

    # Divisão em treino e teste
    X_treino, X_teste, y_treino, y_teste = train_test_split(X, y, test_size=0.25, random_state=3)

    # Avaliação do Naïve Bayes com diferentes parâmetros de suavização
    for smooth in [1e-9, 1e-5]:
        nb_classificador = NBayesClasseUE(suave=smooth)
        nb_classificador.fit(X_treino, y_treino)
        exatidao = nb_classificador.score(X_teste, y_teste)
        resultados.append((dataset, "Naïve Bayes", f"smooth={smooth}", exatidao))

# Exibição dos resultados
print("Resultados de Desempenho:")
for resultado in resultados:
    print(f"Dataset: {resultado[0]}, Algoritmo: {resultado[1]}, Hiperparâmetros: {resultado[2]}, Exatidão: {resultado[3]:.4f}")
