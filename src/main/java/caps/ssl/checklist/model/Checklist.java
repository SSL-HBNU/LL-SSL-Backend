package caps.ssl.checklist.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Setter
public class Checklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checklist_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private caps.ssl.contract.model.Contract contract;

    @OneToMany(
            mappedBy = "checklist",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private List<ChecklistItem> items = new ArrayList<>();

    public void addChecklistItem(ChecklistItem item) {
        items.add(item);
        item.setChecklist(this);
    }

    @Builder
    public Checklist(caps.ssl.contract.model.Contract contract, List<ChecklistItem> items) {
        this.contract = contract;
        for (ChecklistItem item : items) {
            this.addChecklistItem(item);
        }
    }

    public static Checklist createChecklist(caps.ssl.contract.model.Contract contract, List<ChecklistItem> items) {
        return Checklist.builder()
                .contract(contract)
                .items(items)
                .build();
    }
}
