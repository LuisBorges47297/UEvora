%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Este programa avalia a validade de uma jogada de 4 preças de tetris nomeadas por I, S, O e T.                       %
% Como na implementação do jogo deve se levar em conta operações de rotação e translação. Primeiro criar predicados   %
% responsáveis por realizar rotações e translações de cada peças. Mas antes primeiro definir predicados para as       %
% posições iniciais de cada peça.                                                                                     %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%                                     POSICÃO INICIAL DAS PEÇAS E DAS ROTAÇÕES                                        %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Vale obersvar que cada peça ocupa 4 células/pontos da matriz, portanto sempre que falo sobre posição de uma peça,   %
% estou me referindo conjunto de 4 pontos da matriz que especificam o posicionamento da peça.                         %
% Como inicialmente as peças encontram-se no canto superior esquerdo do tabuleiro, então as posições de cada célula   %
% das peças são definidas pela posição do tabuleiro onde as células se encaixam. O predicado                          %
% posicao_peca(Peca, ROT, Posicao), determina a posição de uma peça depois de aplicada a rotação ROT.                 %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Primeira peça.
posicao_peca(i, 0, [(1, 1), (1, 2), (1, 3), (1, 4)]).
posicao_peca(i, 1, [(1, 1), (2, 1), (3, 1), (4, 1)]).
posicao_peca(i, ROT, X) :- NewROT is ROT - 2, posicao_peca(i, NewROT, X).

% Segunda peça
posicao_peca(s, 0, [(2, 1), (1, 2), (2, 2), (1, 3)]).
posicao_peca(s, 1, [(1, 1), (2, 1), (2, 2), (3, 2)]).
posicao_peca(s, ROT, X) :- NewROT is ROT - 2, posicao_peca(s, NewROT, X).

% Terceira peça
posicao_peca(o, _, [(1, 1), (1, 2), (2, 1), (2, 2)]).

% Quarta peça
posicao_peca(t, 0, [(1, 1), (1, 2), (1, 3), (2, 2)]).
posicao_peca(t, 1, [(1, 2), (2, 1), (2, 2), (3, 2)]).
posicao_peca(t, 2, [(1, 2), (2, 1), (2, 2), (2, 3)]).
posicao_peca(t, 3, [(1, 1), (2, 1), (2, 2), (3, 1)]).
posicao_peca(t, ROT, X) :- NewROT is ROT - 4, posicao_peca(t, NewROT, X).

% Pronto, posição inicial das peças e das rotações, feito. Agora é momento de lidar com as translações para direita.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%                                              TRANSLAÇÕES                                                        %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Translações são movimentos das peças de tetris que podem ocorrer para a direita e pela queda da peça até a      %
% posição onde ela para                                                                                           %
% Os predicados translação_linha(PosisaoAtual, NDIR, NovaPosicao) e                                               %
% transacao_coluna(PosicaoAtual, NBXO, NovaPosicao) realizam translação da peça na PosicaoAtual NDIR unidades     %
% para a direita e NBXO unidades para baixo.                                                                      %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
translacao_coluna([], _, NovaPosicao, NovaPosicao). 
translacao_coluna([(L, C)|Resto], NDIR, Acumulador, NovaPosicao) :-
    NovaC is C + NDIR, append(Acumulador, [(L, NovaC)], NovoAcumulador),
     translacao_coluna(Resto, NDIR, NovoAcumulador, NovaPosicao).
translacao_coluna(Posicao, NDIR, NovaPosicao) :- 
    translacao_coluna(Posicao, NDIR, [], NovaPosicao).

translacao_linha([], _, NovaPosicao, NovaPosicao). 
translacao_linha([(L, C)|Resto], NBXO, Acumulador, NovaPosicao) :-
    NovaL is L + NBXO, append(Acumulador, [(NovaL, C)], NovoAcumulador),
        translacao_linha(Resto, NBXO, NovoAcumulador, NovaPosicao).
translacao_linha(Posicao, NBXO, NovaPosicao) :- 
    translacao_linha(Posicao, NBXO, [], NovaPosicao). 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%                                        ESTRUTURA DA MATRIZ DO JOGO                                               %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Estado inicial do jogo é uma matriz NxM que inicialmente fica vazia e é preenchida com o passar do tempo.        %
% No exemplo abixo esta matriz tem dimensão 5x5. Usarei este exemplo para testes do programa.                      %
% Posteriormente essa matriz será gerada dinamicamente de acordo com as dimensões da matriz                        %
% A letra V na matriz abaixo representa espaço vazio.                                                              %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
:- dynamic(n/1).
:- dynamic(m/1).
:- public([n/1, m/1]).
n(4).
m(4).

% Exemplo de tabuleiro de dimensão 3x3
e(
    [
        [v, v, v],
        [v, v, v],
        [v, v, v]
    ]
).

% Este predicado define a dimensão de uma matriz.
definir_dimensao_da_matriz(N, M):-
    retractall(n(_)), retractall(m(_)),
    asserta(n(N)), asserta(m(M)).

% Estes predicados geram uma Matriz vazia de dimensão NxM
gerar_linha(0, Linha, Linha).
gerar_linha(M, LinhaAcumuladora, Linha):-
    NovoM is M - 1,
    gerar_linha(NovoM, [v|LinhaAcumuladora], Linha).

gerar_matriz(0, _, Matriz, Matriz).
gerar_matriz(N, M, MatrizAcumuladora, Matriz):-
    gerar_linha(M, [], Linha),
    NovoN is N - 1,
    gerar_matriz(NovoN, M, [Linha|MatrizAcumuladora], Matriz).
gerar_matriz(N, M, Matriz):-
    gerar_matriz(N, M, [], Matriz).

% Estes predicados imprimem a matriz do jogo de tetris de uma forma agradável
imprimir_linha_de_tracos(0):- print('-'), nl.
imprimir_linha_de_tracos(N):-
    NovoN is N - 1,
    print('----'),
    imprimir_linha_de_tracos(NovoN).
imprimir_linha_de_tracos:- m(M),
    print('      '), print(' '),
    imprimir_linha_de_tracos(M).

imprimir_numero_de_linha(Numero):-
    print('     '), print(Numero), print(' ').

imprimir_numero_de_colunas(Limite, Limite):-
     print(' '), print(Limite), print(' '), !.
imprimir_numero_de_colunas(Atual, Limite):-
    print(' '), print(Atual), print(' '), print(' '),
    NovoAtual is Atual + 1,
    imprimir_numero_de_colunas(NovoAtual, Limite).
imprimir_numero_de_colunas:-
    m(M), print('      '), print(' '), print(' '),
    imprimir_numero_de_colunas(1, M).

imprimir_celula(v):-
    print('| '), print(' '), print(' '), !. 
imprimir_celula(Peca):-
    print('| '), print(Peca), print(' ').

imprimir_linha_da_matriz([]):- print('|'), nl.
imprimir_linha_da_matriz([Peca|Resto]):-
    imprimir_celula(Peca),
    imprimir_linha_da_matriz(Resto).

imprimir_matriz([], _):-
    imprimir_linha_de_tracos, 
    imprimir_numero_de_colunas, nl.
imprimir_matriz([LinhaAtual|Resto], NLinha):-
    imprimir_linha_de_tracos,
    imprimir_numero_de_linha(NLinha),
    imprimir_linha_da_matriz(LinhaAtual),
    NovaNLinha is NLinha + 1,
    imprimir_matriz(Resto, NovaNLinha).
imprimir_matriz(Matriz):-
    nl,
    imprimir_matriz(Matriz, 1).

% Posição inválida
posicao_invalida(P):-
    n(N), m(M),
    NN is N+1,
    NM is M+1,
    P = [(0, 0), (0, 1), (1, 0), (NN, NM)].

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%                                        CONDIÇÕES PARA VALIDADE DE JOGADAS                                       %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Para validade de jogadas, uma série de condições é necessária.                                                  %
% A analogia que uso para verificar se uma jogada é válida a seguinte:                                            %
% CASO DE SUCESSO:                                                                                                %
%    1. Aplicar todos os movimentos iniciais da peça, Rotação e Translação para a direita.                        %
%    2. Verificar se a posição final (depois da rotação e translação para a direita) da peça é válida na Matriz   %
%       (Se nenhuma célula da peça excede os limites de matriz)                                                   %
%    3. Verificar se a posição final (depois da rotação e translação para a direita) não tem nenhuma das células  %
%       ocupadas dentro do tabuleiro.                                                                             %
%    4. Mover a peça para baixo (translatar em linha) até chegar numa posição onde pelo menos uma das células que %
%       ela ocuparia já está ocupada por outra. Nesse movimento fazer o calculo da profundidade de movimento da   %
%       peça ao descer.                                                                                           %
%    5. Retardar uma unidade para cima (com intenção de voltar à última posição válida) e inserir a peça la.      %
% CASO ALTERNATIVO:                                                                                               %
%    2a. Se depois da aplicação da rotação e da translação na peça, ela passa a exceder os limites da matriz.     %
%        então o movimento não é válido.                                                                          %                                                                     %
%    3a. Se tiver uma célula ocupada termina. Nesse ponto a profundidade é 0, e se falha logo em zero, então      %
%        a jogada é inválida.                                                                                     %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Verifica se uma célula está nos limites da matriz
celula_esta_nos_limites((L, C)) :-
    n(N), m(M),
    L >= 1,
    L =< N,
    C >= 1,
    C =< M.

% Verifica se a posição de uma peça está nos limites da matriz
% Uma peça está nos limites da matriz se todas as suas células/pontos estão nos limites.
posicao_valida_na_matriz([C1, C2, C3, C4]):- 
    celula_esta_nos_limites(C1),
    celula_esta_nos_limites(C2),
    celula_esta_nos_limites(C3),
    celula_esta_nos_limites(C4).

sao_celulas_iguais((L1, C1), (L2, C2)):-
    L1 is L2,
    C1 is C2.

posicao_contem_celula([C1, C2, C3, C4], C5):-
    (
        sao_celulas_iguais(C1, C5); 
        sao_celulas_iguais(C2, C5);  
        sao_celulas_iguais(C3, C5);
        sao_celulas_iguais(C4, C5)
    ).

posicao_contem_a_celula_e_esta_ocupada(Posicao, Celula, Valor):- 
    posicao_contem_celula(Posicao, Celula),
    (Valor = i; Valor = s; Valor = o; Valor = t).

% Verifica se na matriz tem céluas ocupadas numa linha, sendo que essas células fazem parte da posição
% de uma peça a inserir
tem_celulas_ocupadas_para_a_posicao_na_linha([], _, _, _):-  fail.
tem_celulas_ocupadas_para_a_posicao_na_linha([Valor|_], NLinha, NColuna, Posicao):-
    posicao_contem_a_celula_e_esta_ocupada(Posicao, (NLinha, NColuna), Valor), !.
tem_celulas_ocupadas_para_a_posicao_na_linha([_|Resto], NLinha, NColuna, Posicao):-
    NovoNColuna is NColuna + 1,
    tem_celulas_ocupadas_para_a_posicao_na_linha(Resto, NLinha, NovoNColuna, Posicao).

% Verifica se na matriz tem células ocupadas na posição especificada pela variável Opcao.
tem_celulas_ocupadas_na_posicao([], _, _, _):- fail.
tem_celulas_ocupadas_na_posicao([Linha|_], NLinha, NColuna, Posicao):-
    tem_celulas_ocupadas_para_a_posicao_na_linha(Linha, NLinha, NColuna, Posicao), !.
tem_celulas_ocupadas_na_posicao([_|Resto], NLinha, NColuna, Posicao):-
    NovaNLinha is NLinha + 1,
    tem_celulas_ocupadas_na_posicao(Resto, NovaNLinha, NColuna, Posicao).

tem_celulas_ocupadas_na_posicao(Matriz, Posicao):-
    tem_celulas_ocupadas_na_posicao(Matriz, 1, 1, Posicao).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Os predicados a seguir determinam a posição de pouso de uma peça.                                                 %
% Como se sabe, no jogo de tetris, quando uma peça é solta, ela cai até fixar-se num ponto onde não possa mais cair.%
% Este predicado determina exatamento a posição onde a peça deve chegar para que não tenha mais espaço para cair.   %
% Se não tiver espaço suficiente para que a peça caiba, então retorna como posição final uma posição inválida.      %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%    
    
determinar_posicao_de_pouso_da_peca(Matriz, PosicaoFinal, PosicaoSeguinte, PosicaoFinal):-
    (\+ posicao_valida_na_matriz(PosicaoSeguinte); tem_celulas_ocupadas_na_posicao(Matriz, PosicaoSeguinte)), !.
determinar_posicao_de_pouso_da_peca(Matriz, _, PosicaoSeguinte, PosicaoFinal):-
    translacao_linha(PosicaoSeguinte, 1, NovaPosicaoSeguinte),
    determinar_posicao_de_pouso_da_peca(Matriz, PosicaoSeguinte, NovaPosicaoSeguinte, PosicaoFinal).

% Este predicado determina a posição de pouso de uma peça. Primeiro é feita a verificação da posição inicial
% da peça para ver se esta não tem algumas células ocupadas. 
%   - Se tiver uma célula ocupada uma posição inválida é retornada como resposta (indicando que não tem onde pousar a peça).
%   - Caso contrário a peça é baixada uma unidade e na nova posição é feita a verifição de validade, continuando
%     com esse processo até que se chegue numa posição inválida ou uma posição com células ocupadas.

determinar_posicao_de_pouso_da_peca(Matriz, PosicaoInicial, PosicaoFinal):-
    (\+ posicao_valida_na_matriz(PosicaoInicial); tem_celulas_ocupadas_na_posicao(Matriz, PosicaoInicial)),
    posicao_invalida(PosicaoFinal), !.
determinar_posicao_de_pouso_da_peca(Matriz, PosicaoInicial, PosicaoFinal):-
    translacao_linha(PosicaoInicial, 1, PosicaoSeguinte),
    determinar_posicao_de_pouso_da_peca(Matriz, PosicaoInicial, PosicaoSeguinte, PosicaoFinal).

% Estes predicados insere uma peça na posição PosicaoPeca dentro da matriz e retorna
% a nova matriz com a peça inserida.

inserir_peca_na_linha_da_matriz([], _, _, _, _, NovaLinha, NovaLinha).
inserir_peca_na_linha_da_matriz([_|RestoLinha], NLinha, NColuna, PosicaoPeca, Peca, LinhaAcumuladora, NovaLinha):-
    posicao_contem_celula(PosicaoPeca, (NLinha, NColuna)),
    NovaNColuna is NColuna + 1,
    append(LinhaAcumuladora, [Peca], NovaLinhaAcumuladora),
    inserir_peca_na_linha_da_matriz(RestoLinha, NLinha, NovaNColuna, PosicaoPeca, Peca, NovaLinhaAcumuladora, NovaLinha), !.
inserir_peca_na_linha_da_matriz([Espaco|RestoLinha], NLinha, NColuna, PosicaoPeca, Peca, LinhaAcumuladora, NovaLinha):-
    NovaNColuna is NColuna + 1,
    append(LinhaAcumuladora, [Espaco], NovaLinhaAcumuladora),
    inserir_peca_na_linha_da_matriz(RestoLinha, NLinha, NovaNColuna, PosicaoPeca, Peca, NovaLinhaAcumuladora, NovaLinha).

inserir_peca_na_matriz([], _, _, _, _, NovaMatriz, NovaMatriz).
inserir_peca_na_matriz([LinhaAtual|RestoMatriz], NLinha, NColuna, PosicaoPeca, Peca, MatrizAcumuladora, NovaMatriz):-
    inserir_peca_na_linha_da_matriz(LinhaAtual, NLinha, NColuna, PosicaoPeca, Peca, [], NovaLinha),
    NovaNLinha is NLinha + 1,
    append(MatrizAcumuladora, [NovaLinha], NovaMatrizAcumuladora),
    inserir_peca_na_matriz(RestoMatriz, NovaNLinha, NColuna, PosicaoPeca, Peca, NovaMatrizAcumuladora, NovaMatriz).
    
inserir_peca_na_matriz(MatrizAtual, PosicaoPeca, Peca, NovaMatriz):-
    inserir_peca_na_matriz(MatrizAtual, 1, 1, PosicaoPeca, Peca, [], NovaMatriz).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%                                           PREDICADOS PRINCIPAIS                                                  %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
validar_jogadas([], _):- !.
validar_jogadas([(Peca, NROT, NDIR)|Resto], Matriz):-
    print('REALIZANDO A JOGADA: '), print((Peca, NROT, NDIR)), nl,
    posicao_peca(Peca, NROT, Posicao),
    translacao_coluna(Posicao, NDIR, PosicaoInicial),
    determinar_posicao_de_pouso_da_peca(Matriz, PosicaoInicial, PosicaoFinal),
    print('- POSIÇÃO RESULTADO: '), print(PosicaoFinal),
    posicao_valida_na_matriz(PosicaoFinal), !, nl,
    inserir_peca_na_matriz(Matriz, PosicaoFinal, Peca, NovaMatriz),
    imprimir_matriz(NovaMatriz), !, nl, nl,
    validar_jogadas(Resto, NovaMatriz).
validar_jogadas(_, _):-!,
    write(' (Posição inválida)'), nl,
    nl,
    write('-------------------------------------------'), nl,
    write('|     SEQUENCIA DE JOGADAS INVÁLIDAS      | '), nl,
    write('-------------------------------------------'), nl,
    fail.
 
paktris(L):- 
    n(N), m(M),
    gerar_matriz(N, M, Matriz), !,
    nl,
    validar_jogadas(L, Matriz), !,
    write('-------------------------------------------'), nl,
    write('|  SUCESSO, SEQUENCIA DE JOGADAS VÁLIDAS  | '), nl,
    write('-------------------------------------------'), nl.


paktris(N, M, L):-
    definir_dimensao_da_matriz(N, M),
    paktris(L).


teste1 :- paktris([(i, 0, 0), (i, 0, 0), (o, 0, 2), (o, 0, 0)]).
teste2 :- paktris([(i, 1, 0), (s, 1, 1), (o, 0, 1)]).