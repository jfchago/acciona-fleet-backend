create view V_CarFleetRequests as
select id, sdn, registration, contract_start, state, cancellation_date, contract_term_months,
       contract_end_date, card_last_four_digits, retired, version, updated_at
from carfleet_requests;
