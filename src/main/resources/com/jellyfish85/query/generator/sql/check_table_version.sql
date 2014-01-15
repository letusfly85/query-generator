set linesize 1000
set pages 10000
set trimspool on
COLUMN table_NAME FORMAT A30
COLUMN table_id   FORMAT A10
COLUMN table_version   FORMAT A10
COLUMN OWNER       FORMAT A15
select
	       com.OWNER				       as owner
	      ,regexp_substr(com.COMMENTS, '[0-9]{5}',1,1,'i') as table_id
	      ,com.TABLE_NAME				       as table_name
	      ,regexp_substr(com.COMMENTS, '[0-9]{9}',1,1,'i') as table_version
FROM
	       all_tab_comments 	   com
where
	      com.OWNER in
(
	      schemaNames
)
order by
	       com.OWNER
	      ,com.TABLE_NAME
;
