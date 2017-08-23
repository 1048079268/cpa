DELETE
FROM
	project_kb_en.kt_cancer;

INSERT INTO project_kb_en.kt_cancer (
	cancer_key,
	doid,
	cancer_name,
	parent_id,
	cancer_definition
) SELECT
		ce_kc.id,
		ce_kc.doid,
		ce_kc.`name`,
		ce_kc.parent_id,
		ce_kc.definition
	FROM
		cpa_en.kt_cancer ce_kc;