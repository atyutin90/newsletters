ALTER TABLE debtor ADD COLUMN document_number VARCHAR(20);
ALTER TABLE debtor_meeting_question ADD COLUMN proposed_solution VARCHAR(500);
ALTER TABLE debtor_meeting_question ADD COLUMN accepted_solution VARCHAR(500);
ALTER TABLE debtor_meeting_question DROP COLUMN answer;
