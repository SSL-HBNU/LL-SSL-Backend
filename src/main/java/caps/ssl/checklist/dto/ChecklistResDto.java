//package caps.ssl.checklist.dto;
//
//import caps.ssl.checklist.model.Checklist;
//import caps.ssl.checklist.model.ChecklistItem;
//import lombok.Getter;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//
//@Getter
//public class ChecklistResDto {
//
//    private Long checklistId;
//    private Long contractId;
//    private List<ChecklistItemResDto> items;
//
//    @Getter
//    public static class ChecklistItemResDto {
//        private Long itemId;
//        private Integer itemNumber;
//        private Boolean isChecked;
//
//        public ChecklistItemResDto(ChecklistItem item) {
//            this.itemId = item.getId();
//            this.itemNumber = item.getItemNumber();
//            this.isChecked = item.isChecked();
//        }
//    }
//
//    public ChecklistResDto(Checklist checklist) {
//        this.checklistId = checklist.getId();
//        this.contractId = checklist.getContract().getId();
//        this.items = checklist.getItems().stream()
//                .map(ChecklistItemResDto::new)
//                .collect(Collectors.toList());
//    }
//}
