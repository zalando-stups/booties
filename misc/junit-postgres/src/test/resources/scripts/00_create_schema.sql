CREATE SCHEMA zmon_eventlog;

CREATE TABLE zmon_eventlog.event_types(
  et_id serial,
  et_name text,
  PRIMARY KEY (et_id)
);

CREATE TABLE zmon_eventlog.events(
  e_type_id int,
  e_created timestamp,
  e_instance_id int,
  e_data jsonb
);

CREATE INDEX ON zmon_eventlog.events USING gin (e_data);
CREATE INDEX ON zmon_eventlog.events (e_created , e_type_id);

INSERT INTO zmon_eventlog.event_types VALUES (213263,'GROUP_MODIFIED'),
                                             (212994,'ALERT_ENDED'),
                                             (212993,'ALERT_STARTED'),
                                             (212996,'ALERT_ENTITY_ENDED'),
                                             (212998,'DOWNTIME_ENDED'),
                                             (213000,'ACCESS_DENIED'),
                                             (212995,'ALERT_ENTITY_STARTED'),
                                             (212999,'SMS_SENT'),
                                             (212997,'DOWNTIME_STARTED'),
                                             (213253,'ALERT_COMMENT_REMOVED'),
                                             (213255,'CHECK_DEFINITION_UPDATED'),
                                             (213256,'ALERT_DEFINITION_CREATED'),
                                             (213251,'TRIAL_RUN_SCHEDULED'),
                                             (213257,'ALERT_DEFINITION_UPDATED'),
                                             (213262,'INSTANTANEOUS_ALERT_EVALUATION_SCHEDULED'),
                                             (213254,'CHECK_DEFINITION_CREATED'),
                                             (213261,'DASHBOARD_UPDATED'),
                                             (213260,'DASHBOARD_CREATED'),
                                             (213258,'CHECK_DEFINITION_DELETED'),
                                             (213249,'DOWNTIME_SCHEDULED'),
                                             (213250,'DOWNTIME_REMOVED'),
                                             (213252,'ALERT_COMMENT_CREATED'),
                                             (213505,'CHECK_DEFINITION_IMPORT_FAILED'),
                                             (213259,'ALERT_DEFINITION_DELETED');
