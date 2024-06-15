ALTER TABLE request_destination ADD COLUMN document_template_id BIGINT;
ALTER TABLE request_destination ADD CONSTRAINT fk_request_destination_document_template_id FOREIGN KEY (document_template_id) REFERENCES document_template (id);

CREATE TABLE IF NOT EXISTS QUESTION_DOCUMENT_TEMPLATE
(
    question_id  BIGINT,
    document_template_id BIGINT,
    CONSTRAINT question_document_template_pkey PRIMARY KEY (question_id, document_template_id),
    CONSTRAINT fk_question_document_template_question_id FOREIGN KEY (question_id) REFERENCES question (id),
    CONSTRAINT fk_question_document_template_document_template_id FOREIGN KEY (document_template_id) REFERENCES document_template (id)
);
