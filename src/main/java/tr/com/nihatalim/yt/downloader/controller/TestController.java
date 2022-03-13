package tr.com.nihatalim.yt.downloader.controller;

import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.nihatalim.yt.downloader.service.StorageService;

@RequestMapping("/api/test")
@RestController
public class TestController {

    private final StorageService storageService;

    public TestController(StorageService storageService) {
        this.storageService = storageService;
    }

    @SneakyThrows
    @GetMapping
    public void a() {
        final String name = "Emir Can İğrek - Dargın (Live Session).mp3";

        storageService.store(name);
    }
}
