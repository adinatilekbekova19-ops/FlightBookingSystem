package kg.demo.flightbookingsystem.service;

import kg.demo.flightbookingsystem.entity.Company;
import kg.demo.flightbookingsystem.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public Company findById(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

    public String uploadLogo(MultipartFile file) throws IOException {
        String uploadDir = "uploads/logos/";
        Path uploadPath = Paths.get(uploadDir);

        String contentType = file.getContentType();

        if (contentType == null ||
                (!contentType.equals("image/jpeg")
                        && !contentType.equals("image/png"))) {
            throw new RuntimeException("Разрешены только JPG и PNG");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("Размер файла не должен превышать 5 МБ");
        }
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, file.getBytes());

        return "/uploads/logos/" + fileName;
    }

    public void updateLogo(Long companyId, String logoPath) {
        Company company = findById(companyId);
        if (company != null) {
            company.setLogoPath(logoPath);
            companyRepository.save(company);
        }
    }
}