set linesize 1000
set pages 10000
set trimspool on
COLUMN OBJECT_NAME FORMAT A30
COLUMN OBJECT_TYPE FORMAT A30
COLUMN OWNER       FORMAT A15
select
	s.OWNER
	,s.OBJECT_TYPE
	,s.OBJECT_NAME
from
	dba_objects s
where
	s.OWNER in
(
	'schemaNames'
)
order by
	s.OWNER
	,s.OBJECT_TYPE
	,s.OBJECT_NAME
;
