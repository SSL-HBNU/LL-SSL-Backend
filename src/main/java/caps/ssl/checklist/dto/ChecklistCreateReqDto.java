package caps.ssl.checklist.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChecklistCreateReqDto {
    private Long contractId;

    @Valid
    @Size(min = 8, max = 8, message = "체크리스트 항목은 8개여야 합니다")
    private List<ChecklistItemReqDto> items;

    @Getter
    @Setter
    public static class ChecklistItemReqDto {
        @Min(0)
        @Max(7)
        private Integer itemNumber;

        @NotNull
        private Boolean isChecked;
    }
}
