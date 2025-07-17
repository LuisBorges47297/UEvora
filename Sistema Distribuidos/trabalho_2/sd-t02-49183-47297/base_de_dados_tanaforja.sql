CREATE DATABASE TaNaForja;

CREATE TABLE utilizador (
    id SERIAL NOT NULL,
    username VARCHAR(100),
    email VARCHAR(100),
    password VARCHAR(100),
    tipo VARCHAR(100),
    data TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT user_pk PRIMARY KEY (id)
);

CREATE TABLE localizacao (
    id SERIAL NOT NULL,
    latitude int NOT NULL,
    longitude int NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT localizacao_pk PRIMARY KEY (id)
);

CREATE TABLE tipo_de_arte (
    id SERIAL NOT NULL,
    descricao VARCHAR(200) NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT tipo_de_arte_pk PRIMARY KEY (id)
);

CREATE TABLE artista (
    id SERIAL NOT NULL,
    nome VARCHAR(200) NOT NULL,
    descricao VARCHAR(200) NOT NULL,
    foto VARCHAR(200) NOT NULL,
    tipo_de_arte INT NOT NULL,
    esta_apurado INT DEFAULT 0,
    data TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT artista_pk PRIMARY KEY (id),
    CONSTRAINT artista_fk_arte FOREIGN KEY (tipo_de_arte) REFERENCES tipo_de_arte(id)
);

CREATE TABLE atuacao (
    id SERIAL NOT NULL,
    data TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    localizacao INT NOT NULL,
    artista INT NOT NULL,
    atuando INT NOT NULL,
    CONSTRAINT atuacao_pk PRIMARY KEY (id),
    CONSTRAINT atuacao_fk_localizacao FOREIGN KEY (localizacao) REFERENCES localizacao(id),
    CONSTRAINT atuacao_fk_artista FOREIGN KEY (artista) REFERENCES artista(id)
);

CREATE TABLE donativo (
    id SERIAL NOT NULL,
    artista INT NOT NULL,
    data TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    valor money NOT NULL,
    utilizador int NOT NULL,
    CONSTRAINT donativo_pk PRIMARY KEY (id),
    CONSTRAINT donativo_fk_artista FOREIGN KEY (artista) REFERENCES artista(id),
    CONSTRAINT donativo_fk_user FOREIGN KEY (utilizador) REFERENCES utilizador(id)
);


CREATE TABLE classificacao (
    id SERIAL NOT NULL,
    artista INT NOT NULL,
    classe FLOAT NOT NULL,
    comentario VARCHAR(200) NULL,
    utilizador INT NOT NULL,
    CONSTRAINT classificacao_pk PRIMARY KEY (id),
    CONSTRAINT classificacao_fk_artista FOREIGN KEY (artista) REFERENCES artista(id),
    CONSTRAINT classificacao_fk_utilizador FOREIGN KEY (utilizador) REFERENCES utilizador(id)
);

/* Utilizador */
INSERT INTO utilizador (username, email, password, tipo) 
    VALUES ('luiz', 'luiz@gmail.com', 'luiz', 'UTILIZADOR_GERAL'),
    ('thawila', 'thawila@gmail.com', 'thawila', 'ADMINISTRADOR');

/* LOcalização */
INSERT INTO localizacao (latitude, longitude) VALUES (2000, 2000);
INSERT INTO localizacao (latitude, longitude) VALUES (3000, 3000);

/* Tipo de arte */
INSERT INTO tipo_de_arte (descricao) VALUES ('Música'), ('Dança'), ('Pintura');

/* Artista */
INSERT INTO artista (nome, descricao, foto, tipo_de_arte, esta_apurado) VALUES ('Alicia Patricio', 'Grande cantora', "p.png", 1, 0), 
    ('Luisa Manuela', 'Grande cantora', "p.png", 1, 0);

/* Atuação */
INSERT INTO atuacao(localizacao, artista, atuando) VALUES (1, 1, 1), (2, 2, 1);

/* Classificação */
INSERT INTO classificacao(artista, classe, comentario, utilizador) VALUES (1, 7.6, 'Bom contor', 1), (2, 8.6, 'Parabéns', 1);

