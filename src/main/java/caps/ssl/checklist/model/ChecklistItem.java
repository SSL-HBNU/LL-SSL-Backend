package caps.ssl.checklist.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@Setter
public class ChecklistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private Integer itemNumber;
    private boolean isChecked;

    @Column(columnDefinition = "TEXT")
    private String guide;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checklist_id")
    private Checklist checklist;

    @Builder
    public ChecklistItem(Integer itemNumber, boolean isChecked, String guide) {
        this.itemNumber = itemNumber;
        this.isChecked = isChecked;
        this.guide = guide;
    }
}
