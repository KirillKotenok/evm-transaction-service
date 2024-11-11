CREATE TABLE public.sync_state
(
    last_processed_block         BIGINT NOT NULL,
    CONSTRAINT PK_customer PRIMARY KEY (last_processed_block)
);
