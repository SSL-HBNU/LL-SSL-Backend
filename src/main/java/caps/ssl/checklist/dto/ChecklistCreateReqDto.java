package caps.ssl.checklist.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ChecklistCreateReqDto {

    private Long contractId;
    private List<ChecklistItemDto> items;

    // 내부 클래스로 각 체크 항목을 표현
    @Getter
    @NoArgsConstructor
    public static class ChecklistItemDto {
        private Integer itemNumber;
        private Boolean isChecked;
    }
}
