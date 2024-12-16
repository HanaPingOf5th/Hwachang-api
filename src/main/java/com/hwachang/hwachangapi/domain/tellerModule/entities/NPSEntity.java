package com.hwachang.hwachangapi.domain.tellerModule.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

@Entity
@Immutable
// Todo: View 생성 후 처리
//@Subselect("select count(*) sum_customer, avg(nps) avg_score, t.id teller_id" +
//        "from teller t" +
//        "join review r on t.id = r.banker_id" +
//        "where t.id = ?")
@Table(name = "NPS")
public class NPSEntity {
    @Id
    @Column(name="nps_id")
    private String id;
    private Integer sumCustomer;
    private Integer avgScore;
}