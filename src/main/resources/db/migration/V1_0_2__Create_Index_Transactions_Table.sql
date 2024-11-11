-- Index on from_address for efficient lookups by sender address
CREATE INDEX idx_transactions_from_address ON public.transactions (from_address);

-- Index on to_address for efficient lookups by recipient address
CREATE INDEX idx_transactions_to_address ON public.transactions (to_address);

-- Index on block_number for efficient retrieval of transactions by block
CREATE INDEX idx_transactions_block_number ON public.transactions (block_number);

-- Index on timestamp to optimize time-based queries (e.g., retrieving recent transactions)
CREATE INDEX idx_transactions_timestamp ON public.transactions (created_at);

-- Composite index on from_address and to_address for faster lookups on both sender and recipient
CREATE INDEX idx_transactions_from_to ON public.transactions (from_address, to_address);