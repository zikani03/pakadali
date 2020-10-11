Pakadali
========

Pakadali is toy project that provides some APIs that may be useful to developers.
The following services are available:

* Placeholder Image generation
* QR Code generator
* Converting a WhatsApp Chat text file to JSON

## Usage

### Placeholder Image generation

```sh
$ curl -G https://pakadali.herokuapp.com/img/400x400
```

### QR Code generator

```sh
$ curl -G "https://pakadali.herokuapp.com/qr/generate/100?content=The%20text%20in%20the%20QR%20code" -o image.png
```

### WhatsApp Group Chat to JSON

There's a form here [https://pakadali.herokuapp.com/wa2json.html](https://pakadali.herokuapp.com/wa2json.html)

Or you can do it programmatically

```sh
$ curl -X POST -F "file=@./ExportedChat.txt" https://pakadali.herokuapp.com/wa/chat2json
```


## Building and running locally

This project requires JDK 15+ to build.

```sh
$ git clone https://github.com/zikani03/pakadali
$ cd pakadali
$ ./gradlew build
$ java -Dspark.port=8080 -jar build/libs/pakadali-0.1.0-SNAPSHOT-all.jar
```

Try visiting `http://localhost:8080/img/500x500` you should get 500 x 500 PNG image.

----

Copyright (c) 2020, Zikani Nyirenda Mwase
