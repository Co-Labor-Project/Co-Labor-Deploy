package pelican.co_labor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pelican.co_labor.domain.enterprise.Enterprise;
import pelican.co_labor.domain.enterprise.EnterpriseEng;
import pelican.co_labor.repository.enterprise.EnterpriseEngRepository;
import pelican.co_labor.repository.enterprise.EnterpriseRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EnterpriseService {

    private final EnterpriseRepository enterpriseRepository;
    private final EnterpriseEngRepository enterpriseEngRepository;

    @Autowired
    public EnterpriseService(EnterpriseRepository enterpriseRepository, EnterpriseEngRepository enterpriseEngRepository) {
        this.enterpriseRepository = enterpriseRepository;
        this.enterpriseEngRepository = enterpriseEngRepository;
    }

    public List<Enterprise> getAllEnterprises() {
        return enterpriseRepository.findAll();
    }

    public List<EnterpriseEng> getAllEnterprisesEng() {
        return enterpriseEngRepository.findAll();
    }

    public Optional<Enterprise> getEnterpriseById(String enterpriseId) {
        return enterpriseRepository.findById(enterpriseId);
    }

    public Optional<EnterpriseEng> getEnterpriseEngById(String enterpriseId) {
        return enterpriseEngRepository.findById(enterpriseId);
    }

    public Enterprise createEnterprise(Enterprise enterprise) {
        return enterpriseRepository.save(enterprise);
    }

    public Optional<Enterprise> updateEnterprise(String enterpriseId, Enterprise enterpriseDetails) {
        return enterpriseRepository.findById(enterpriseId).map(enterprise -> {
            enterprise.setName(enterpriseDetails.getName());
            enterprise.setAddress1(enterpriseDetails.getAddress1());
            enterprise.setAddress2(enterpriseDetails.getAddress2());
            enterprise.setDescription(enterpriseDetails.getDescription());
            enterprise.setType(enterpriseDetails.getType());
            enterprise.setPhone_number(enterpriseDetails.getPhone_number());
            return enterpriseRepository.save(enterprise);
        });
    }

    public String saveImage(MultipartFile image) throws IOException {
        String imageDirectory = "path/to/save/images";
        String imageName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
        Path imagePath = Paths.get(imageDirectory, imageName);
        Files.createDirectories(imagePath.getParent());
        Files.write(imagePath, image.getBytes());
        return imageName;
    }
}
