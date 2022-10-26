create temp table gugik_cemeteries_to_remove as
select id_iip, cemetery_id
from gugik_cemetery_geometries gcg
where name like '%zwierz%' or name like '%dla koni%' or description like '%zwierz%' or description like '%dla koni%';

delete
from gugik_cemetery_geometries gcg
where id_iip in (select id_iip
                from gugik_cemeteries_to_remove);

delete
from gugik_cemeteries gc
where id in (select cemetery_id
            from gugik_cemeteries_to_remove);

create temp table cemeteries_to_remove as
select id_iip_identifier, cemetery_id
from cemetery_geometries cg
where id_iip_identifier in (select split_part(id_iip, '_', 1) || '_' || split_part(id_iip, '_', 2)
                           from gugik_cemeteries_to_remove);

delete
from cemetery_geometries cg
where id_iip_identifier in (select id_iip_identifier
                            from cemeteries_to_remove);

delete
from cemeteries c
where id in (select cemetery_id
            from cemeteries_to_remove);
