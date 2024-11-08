CREATE SEQUENCE public.sync_state_generator;

ALTER SEQUENCE public.sync_state_generator INCREMENT BY 50;

CREATE TABLE public.sync_state
(
    id         BIGINT DEFAULT NEXTVAL('sync_state_generator'),
    last_processed_block    BIGINT NOT NULL,
    CONSTRAINT PK_customer PRIMARY KEY (id)
);
