Pakadali
========

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

----

Copyright (c) 2020, Zikani Nyirenda Mwase
