package caps.ssl.contract.model;

import caps.ssl.chat.model.ChatRoom;
import caps.ssl.checklist.model.Checklist;
import caps.ssl.contract.dto.Issue;
import caps.ssl.law.model.LawInfo;
import caps.ssl.member.model.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Setter
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String imagePath;

    @Lob
    private String ocrText;

    @Column(columnDefinition = "TEXT")
    private String issuesJson;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "contract_issues", joinColumns = @JoinColumn(name = "contract_id"))
    private List<Issue> issues = new ArrayList<>();

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<LawInfo> lawInfos = new ArrayList<>();

    @OneToOne(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private Checklist checklist;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoom> chatRooms = new ArrayList<>();

}