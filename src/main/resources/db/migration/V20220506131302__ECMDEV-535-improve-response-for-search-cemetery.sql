update cemeteries set open_term_type = 'YEAR' where open_term_type is null;
update cemeteries set substitute_performance = false where substitute_performance is null;
update cemeteries set church_perpetual_user = false where church_perpetual_user is null;
update cemeteries set church_owner = false where church_owner is null;
update cemeteries set church_regulated_by_law = false where church_regulated_by_law  is null;
update cemeteries set manager_exists = false where manager_exists is null;
update cemeteries set user_admin_exists = false where user_admin_exists is null;