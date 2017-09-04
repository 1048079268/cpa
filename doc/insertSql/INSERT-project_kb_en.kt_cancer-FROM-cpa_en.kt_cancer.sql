-- clear database
DELETE FROM project_kb_en.kt_cancer;
-- insert data
INSERT INTO project_kb_en.kt_cancer (
	cancer_key,
	doid,
	cancer_name,
	parent_ids,
	cancer_definition
) SELECT
		c.id,
		c.doid,
		c.`name`,
		GROUP_CONCAT(c.parent_id),
		c.definition
	FROM
		cpa_en.kt_cancer c
	GROUP BY
		c.doid
	ORDER BY
		c.doid;
-- update parent_keys
UPDATE project_kb_en.kt_cancer c0
	INNER JOIN (
							 SELECT
								 c2.doid doid,
								 GROUP_CONCAT(c1.cancer_key) parent_keys
							 FROM
								 project_kb_en.kt_cancer c1,
								 cpa_en.kt_cancer c2
							 WHERE
								 c1.doid = c2.parent_id
							 GROUP BY
								 c2.doid
							 ORDER BY
								 c2.doid
						 ) c3 ON c0.doid = c3.doid
SET c0.parent_keys = c3.parent_keys;