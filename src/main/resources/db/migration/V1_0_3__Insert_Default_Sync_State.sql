DO $$
    BEGIN
        -- Check if the table is empty
        IF NOT EXISTS (SELECT 1 FROM public.sync_state) THEN
            -- Insert the value 0 into the table
            INSERT INTO public.sync_state (last_processed_block)
            VALUES (21159105);
        END IF;
    END $$;