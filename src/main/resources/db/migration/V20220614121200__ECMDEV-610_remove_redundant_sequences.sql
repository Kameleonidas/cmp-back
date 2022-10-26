ALTER TABLE permissions ALTER COLUMN id SET DEFAULT nextval('permissions_id_seq'::regclass);

ALTER TABLE messages ALTER COLUMN id SET DEFAULT nextval('messages_id_seq'::regclass);

ALTER TABLE message_parameters ALTER COLUMN id SET DEFAULT nextval('message_parameters_id_seq'::regclass);

ALTER TABLE user_account_activity_periods ALTER COLUMN id SET DEFAULT nextval('user_account_activity_periods_id_seq'::regclass);

ALTER TABLE user_account_statuses_dictionary ALTER COLUMN id SET DEFAULT nextval('user_account_statuses_dictionary_id_seq'::regclass);

ALTER TABLE user_account_to_subjects ALTER COLUMN id SET DEFAULT nextval('user_account_to_subjects_id_seq'::regclass);

ALTER TABLE user_account_to_subjects ALTER COLUMN id SET DEFAULT nextval('user_account_to_subjects_id_seq'::regclass);

ALTER TABLE email_senders ALTER COLUMN id SET DEFAULT nextval('email_senders_id_seq'::regclass);

