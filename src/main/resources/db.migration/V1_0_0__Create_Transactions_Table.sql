CREATE TABLE public.transactions
(
    hash             VARCHAR(66)    NOT NULL,
    from_address     VARCHAR(42)    NOT NULL,
    to_address       VARCHAR(42),
    value            NUMERIC(30, 0) NOT NULL,
    block_number     BIGINT         NOT NULL,
    raw_block_number VARCHAR        NOT NULL,
    timestamp        TIMESTAMP      NOT NULL default now(),
    CONSTRAINT PK_transactions PRIMARY KEY (hash)
);
