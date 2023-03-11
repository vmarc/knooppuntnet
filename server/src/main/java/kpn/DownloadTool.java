package kpn;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class DownloadTool {
    public static void main(String[] args) {
        new DownloadTool().download();
    }

    public void download() {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://geodata.toerismevlaanderen.be")
                .build();
        download(webClient, "knoop_wandel", "/tmp/knoop_wandel.xml");
        download(webClient, "traject_wandel", "/tmp/traject_wandel.xml");
        download(webClient, "verkeersintensiteit_wandel", "/tmp/verkeersintensiteit_wandel.xml");
        download(webClient, "wegdek_wandel", "/tmp/wegdek_wandel.xml");
    }

    private void download(WebClient webClient, String what, String destinationFilename) {
        System.out.println("download " + what);
        Flux<DataBuffer> dataBuffer = webClient
                .get()
                .uri("/geoserver/wfs?service=WFS&version=1.1.0&request=getfeature&typeNames=" + what)
                .retrieve()
                .bodyToFlux(DataBuffer.class);
        Path destination = Path.of(destinationFilename);
        DataBufferUtils.write(dataBuffer, destination, StandardOpenOption.CREATE).share().block();
    }
}