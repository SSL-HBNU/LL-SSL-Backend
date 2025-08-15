//package caps.ssl.checklist.model;
//
//import caps.ssl.contract.model.Contract;
//import jakarta.persistence.*;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Getter
//@NoArgsConstructor
//public class Checklist {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "checklist_id")
//    private Long id;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "contract_id")
//    private Contract contract;
//
//    @OneToMany(mappedBy = "checklist", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ChecklistItem> items = new ArrayList<>();
//
//    @Builder
//    public Checklist(Contract contract, List<ChecklistItem> items) {
//        this.contract = contract;
//        for (ChecklistItem item : items) {
//            this.addChecklistItem(item);
//        }
//    }
//
//    public void addChecklistItem(ChecklistItem checklistItem) {
//        items.add(checklistItem);
//        checklistItem.setChecklist(this);
//    }
//
//    public static Checklist createChecklist(Contract contract, List<ChecklistItem> items) {
//        return Checklist.builder()
//                .contract(contract)
//                .items(items)
//                .build();
//    }
//}