CREATE TABLE delegates (
  id                INTEGER PRIMARY KEY,
  public_key        VARCHAR UNIQUE NOT NULL,
  node_id           VARCHAR UNIQUE NOT NULL,
  address           VARCHAR        NOT NULL,
  host              VARCHAR        NOT NULL,
  port              INTEGER        NOT NULL,
  registration_date BIGINT         NOT NULL
);

CREATE TABLE delegate2genesis (
  delegate_id INTEGER NOT NULL REFERENCES delegates,
  genesis_id  INTEGER NOT NULL REFERENCES genesis_blocks,
  PRIMARY KEY (delegate_id, genesis_id)
);