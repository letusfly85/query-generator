set linesize 1000
set pages 10000
set trimspool on
COLUMN OBJECT_NAME FORMAT A30
COLUMN OBJECT_TYPE FORMAT A30
COLUMN OWNER       FORMAT A15
select
    owner,
    object_type,
    object_name
from
    dba_objects x
where
    x.object_type <> 'SYNONYM'
and x.owner in
(
	schemaNames
)
and x.object_name in
(
    select
         s.OBJECT_NAME
    from
        dba_objects s
    where
        s.OWNER in
    (
        schemaNames
    )
    group by
         s.OBJECT_NAME
    having
        count(*) > 1
)
order by
	 x.OWNER
	,x.OBJECT_TYPE
	,x.OBJECT_NAME
;