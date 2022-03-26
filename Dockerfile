FROM adoptopenjdk/openjdk11

WORKDIR /app

COPY ./target/downloader.jar ./downloader.jar

RUN apt-get update

RUN apt install python3 ffmpeg -y

RUN curl -L https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp -o /usr/bin/youtube-dl

RUN chmod a+rx /usr/bin/youtube-dl

ENTRYPOINT [ "java", "-jar", "/app/downloader.jar"]