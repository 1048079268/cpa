package com.todaysoft.cpa.domain.en.evidence;

import com.todaysoft.cpa.domain.entity.EvidenceReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/14 15:42
 */
public interface EvidenceReferenceRepository extends JpaRepository<EvidenceReference,String>{
    List<EvidenceReference> findByEvidenceKey(String evidenceKey);
}
