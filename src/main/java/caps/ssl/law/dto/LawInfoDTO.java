package caps.ssl.law.dto;

import caps.ssl.law.model.LawInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LawInfoDTO {
    private String lawName;
    private String sourceLink;
    private Long lawId;

    public LawInfoDTO(LawInfo lawInfo) {
        this.lawId = lawInfo.getId();
        this.lawName = lawInfo.getLawName();
        this.sourceLink = "https://www.law.go.kr" + lawInfo.getDetailUrl();
    }
}
