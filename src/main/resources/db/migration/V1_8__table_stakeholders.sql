CREATE TABLE stakeholders (
  id         INTEGER PRIMARY KEY,
  address    VARCHAR NOT NULL UNIQUE,
  public_key VARCHAR NOT NULL UNIQUE
);

CREATE TABLE stakeholders_2_delegates (
  stakeholder_id BIGINT NOT NULL REFERENCES stakeholders,
  delegate_id    BIGINT NOT NULL REFERENCES delegates,
  PRIMARY KEY (stakeholder_id, delegate_id)
);