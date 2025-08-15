package caps.ssl.checklist.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ChecklistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private Integer itemNumber;

    private boolean isChecked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checklist_id")
    private Checklist checklist;

    public void setChecklist(Checklist checklist) {
        this.checklist = checklist;
    }

    @Builder
    public ChecklistItem(Integer itemNumber, boolean isChecked){
        this.itemNumber = itemNumber;
        this.isChecked = isChecked;
    }
}