CREATE TABLE IF NOT EXISTS REQUEST_ACCOUNT
(
    request_id  BIGINT,
    account VARCHAR(100),
    CONSTRAINT request_account_pkey PRIMARY KEY (request_id, account),
    CONSTRAINT fk_request_account_request_id FOREIGN KEY (request_id) REFERENCES request (id)
);
