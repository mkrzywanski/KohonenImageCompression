# KohonenImageCompression [![Build Status](https://travis-ci.org/sHiniz0r/KohonenImageCompression.svg?branch=master)](https://travis-ci.org/sHiniz0r/KohonenImageCompression)
This project aims to compress square images using Kohonen neural network.
## Technolgies
Application implemented using Java 8 and Maven.
## Requirements
Requirements :
1. 	Image needs to have same width and height.
2. 	Image's width and height values need to be divisable by two without any remainder.

## Compression evaluation :

1. 	PSNR - [PSNR](https://en.wikipedia.org/wiki/Peak_signal-to-noise_ratio) 
2. 	Compression rate - a factor given by original image's bits number divised by compressed image's bits number.

## Examples
Original Image :
![LenaOriginal](https://i.imgur.com/ysnLqp6.jpg)

Compressed image with Compression rate = 20.07843137254902 and PSNR = 12.95185660017397
![LenaCompressed1](https://i.imgur.com/Swub3uS.jpg)

Compressed image with Compression rate = 10.014669926650367 and PSNR = 19.622227898861464 :
![LenaCompressed2](https://i.imgur.com/itkq1HY.jpg)
