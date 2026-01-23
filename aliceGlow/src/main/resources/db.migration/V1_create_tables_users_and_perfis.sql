CREATE TABLE users (
    id bigserial NOT NULL,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(150) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE perfis(
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(50)NOT NULL UNIQUE
);


CREATE TABLE user_perfil(
    user_id BIGINT NOT NULL,
    perfil_id BIGINT NOT NULL,,
    PRIMARY KEY(user_id, perfil_id),

    CONSTRAINT fk_user_perfis_user
        FOREIGN KEY(user_id) REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_perfis_perfil
        FOREIGN KEY (perfil_id) REFERENCES perfis(is)
        ON DELETE CASCADE
);

