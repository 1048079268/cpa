DELETE
FROM
	project_kb_cn.kt_cancer;
INSERT INTO project_kb_cn.kt_cancer (
	cancer_key,
	doid,
	cancer_name,
	parent_id,
	cancer_definition
) SELECT
	cc_kc.id,
	cc_kc.doid,
	cc_kc.`name`,
	cc_kc.parent_id,
	cc_kc.definition
FROM
	cpa_cn.kt_cancer cc_kc;