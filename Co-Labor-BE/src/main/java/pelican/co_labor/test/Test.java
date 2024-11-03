//package pelican.co_labor.test;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import pelican.co_labor.domain.enterprise.Enterprise;
//import pelican.co_labor.repository.enterprise.EnterpriseRepository;
//
//import java.util.List;
//
//@Component
//public class Test implements CommandLineRunner {
//
//    private final EnterpriseRepository enterpriseRepository;
//
//    @Autowired
//    public Test(EnterpriseRepository enterpriseRepository) {
//        this.enterpriseRepository = enterpriseRepository;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        List<Enterprise> enterprises = enterpriseRepository.findAll();
//
//        for (Enterprise enterprise : enterprises) {
//            // description이 null인 경우에만 업데이트
//            if (enterprise.getDescription() == null || enterprise.getDescription().isEmpty()) {
//                String description = generateDescription(enterprise.getName(), enterprise.getType());
//                enterprise.setDescription(description);
//                enterpriseRepository.save(enterprise);
//                System.out.println("Updated enterprise ID: " + enterprise.getEnterprise_id() + " with description: " + description);
//            }
//        }
//    }
//
//    // 기업 이름과 분류에 기반한 설명 생성
//    private String generateDescription(String name, String type) {
//        switch (type) {
//            case "서비스업":
//                return String.format("%s는 고객 중심의 서비스를 제공하는 서비스업체입니다. 다양한 고객 요구에 부응하여 맞춤형 솔루션을 제공합니다.", name);
//            case "금융/은행업":
//                return String.format("%s는 안전하고 신뢰할 수 있는 금융 서비스를 제공합니다. 고객의 자산을 보호하고, 재정 계획을 돕습니다.", name);
//            case "IT/정보통신업":
//                return String.format("%s는 최신 기술을 활용한 IT 솔루션을 제공합니다. 혁신적인 정보통신 서비스를 통해 고객의 비즈니스를 지원합니다.", name);
//            case "판매/유통업":
//                return String.format("%s는 고객에게 최고의 제품과 서비스를 제공하는 판매/유통업체입니다. 빠르고 효율적인 유통망을 자랑합니다.", name);
//            case "제조/생산/화학업":
//                return String.format("%s는 고품질의 제품을 제조하며, 환경 친화적인 생산 과정을 통해 지속 가능한 발전을 추구합니다.", name);
//            case "교육업":
//                return String.format("%s는 다양한 교육 프로그램을 제공하며, 미래 인재 양성을 위해 최선을 다하는 교육기관입니다.", name);
//            case "건설업":
//                return String.format("%s는 안전하고 혁신적인 건설 솔루션을 제공하는 건설업체입니다. 품질과 신뢰성을 최우선으로 생각합니다.", name);
//            case "의료/제약업":
//                return String.format("%s는 고객의 건강을 최우선으로 생각하며, 고품질의 의료 서비스를 제공합니다.", name);
//            case "미디어/광고업":
//                return String.format("%s는 창의적이고 혁신적인 미디어 솔루션을 제공하여 브랜드 가치를 높이는 광고 전문 업체입니다.", name);
//            case "문화/예술/디자인업":
//                return String.format("%s는 문화와 예술을 통해 사람들과 소통하며, 독창적인 디자인 서비스를 제공합니다.", name);
//            case "기관/협회":
//                return String.format("%s는 해당 분야의 발전을 위해 활동하는 기관으로, 회원들의 권익을 보호하고 지식을 공유합니다.", name);
//            default:
//                return String.format("%s는 다양한 서비스를 제공하는 기업입니다.", name);
//        }
//    }
//}
