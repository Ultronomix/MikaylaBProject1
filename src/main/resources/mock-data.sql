INSERT INTO ers_user_roles (role_id, role)
VALUES
('531f46a6-3859-44f6-90ad-f45596e025fa', 'EMPLOYEE'),
('d5fad1fc-2a8b-43ba-90f4-0b6741afe447', 'MANAGER'),
('3cbd7cf4-e0ce-4b70-92ca-3ec53643e563', 'ADMIN');

INSERT INTO ers_users (given_name, surname, email, username, "password", role_id)
VALUES
('Bob', 'Bailey', 'bob.bailey@revature.com', 'bbailey', 'p4$SW0RD', '531f46a6-3859-44f6-90ad-f45596e025fa'),
('Charles', 'Cantrell', 'charles.cantrell@revature.com', 'ccantrell', 'p4S$W0RD', 'd5fad1fc-2a8b-43ba-90f4-0b6741afe447'),
('Emily', 'Erikson', 'emily.erikson@revature.com', 'eerikson', 'p4$$word', '3cbd7cf4-e0ce-4b70-92ca-3ec53643e563');

INSERT INTO ers_reimbursement_types (type)
VALUES
('LODGING'),
('TRAVEL'),
('FOOD'),
('OTHER');

INSERT INTO ers_reimbursement_statuses (status)
VALUES
('PENDING'),
('APPROVED'),
('DENIED');

INSERT INTO ers_reimbursements (reimb_id, amount, submitted, resolved, description, payment_id, author_id, resolver_id, status_id, type_id);