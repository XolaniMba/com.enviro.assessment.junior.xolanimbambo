INSERT INTO investors (id, first_name, last_name, id_number, email, date_of_birth) VALUES
  (1, 'Thabo', 'Nkosi', '5803125800089', 'thabo.nkosi@example.com', '1958-03-12'),
  (2, 'Lindiwe', 'Dlamini', '9202140800083', 'lindiwe.dlamini@example.com', '1992-02-14');

INSERT INTO investment_products (id, account_number, product_type, balance, investor_id) VALUES
  (1, 'RA-0001-001', 'RETIREMENT_ANNUITY', 850000.00, 1),
  (2, 'DI-0001-002', 'DISCRETIONARY_INVESTMENT', 120000.00, 1),
  (3, 'DI-0002-001', 'DISCRETIONARY_INVESTMENT', 45000.00, 2);