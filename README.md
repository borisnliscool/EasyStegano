# EasyStegano
EasyStegano is a Steganography tool that uses AES encryption to encrypt the message.
This project was created in 2 days, with fully documenting it in another. 
There are definitely improvements to be made like a better UI but it works, and that's most important.

## Steganography
Steganography is the art of hiding a message in another message. Here we will be hiding our message in an image. People will probably not suspect a secret message hidden in an image. Normal steganography is no means of encryption, just a way of hiding data inside an image. But with this project, I encrypt the message with AES allowing you to set a password for a message.

More on steganography [on wikipedia](https://en.wikipedia.org/wiki/Steganography).

## Details
The User chooses an image, the image data is then normalized, meaning that each RGB value is decremented by one if it is not even. This is done for every pixel in the image.

Next, the message is converted to a binary representation, 8 Bits per character of the message. This binary representation is then applied to the normalized image, 3 Bit per pixel. This concludes, that the maximal length of a message hidden in an image is:

Since the image was normalized, we now know that an even r, g, or b value is 0 and an uneven is a 1. And this is how the message is decoded back from the image.

- explenation by [stylesuxx](https://github.com/stylesuxx)

Our message is also encrypted by AES encryption so even if someone knows there's a hidden message in the image they still can't read it.
