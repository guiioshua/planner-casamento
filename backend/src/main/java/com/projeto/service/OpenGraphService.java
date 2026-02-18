package com.projeto.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OpenGraphService {

    public String extractImage(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                            "AppleWebKit/537.36 (KHTML, like Gecko) " +
                            "Chrome/100.0.0.0 Safari/537.36")
                    .timeout(5000)
                    .get();

            Element ogImage = doc.selectFirst("meta[property=og:image]");
            if (ogImage != null && hasContent(ogImage)) {
                return ogImage.attr("content");
            }

            Element twitterImage = doc.selectFirst("meta[name=twitter:image]");
            if (twitterImage != null && hasContent(twitterImage)) {
                return twitterImage.attr("content");
            }

            Element firstImg = doc.selectFirst("img[src^=http]"); // Only absolute URLs
            if (firstImg != null) {
                return firstImg.attr("src");
            }

            return null;

        } catch (IOException e) {
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private boolean hasContent(Element element) {
        String content = element.attr("content");
        return !content.isBlank();
    }
}