package com.etz;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.jcajce.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Iterator;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {

        if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }

        String passPhrase = "EES13140036@oou";

        PGPPublicKey publicKeyArmored = loadPublicKeyFromString("-----BEGIN PGP PUBLIC KEY BLOCK-----\n" +
              "Version: Keybase OpenPGP v2.0.76\n" +
              "Comment: https://keybase.io/crypto\n" +
              "\n" +
              "xsFNBGT3Dr8BEADZOzWDmeppf/w7IlkfX0B6o8Hm7ai55mNDbJZ8qieYupgrIXuH\n" +
              "A6cXLwee1OrucO9+/CAatZ82AOuIOT7CrZlJMk6KcaCNkL9Ta1/+5peu8HU+5WQN\n" +
              "ckJ8QhoxpUq5y5vxJV0k2SrDYIC0kq/Cpl8WyTW2pab+OBoHv4pzP6PCmvtBD4ZI\n" +
              "wPTekMY/pOskE80suGb46+08qRSvfAPq+TZCOFfra+hsr6sa6nKIr/FwZuaBewMu\n" +
              "zM8yK0bbH4yQCExZwIQprdwPEYyhG/bWrNqhgPMTsNLTeSiMziZSX5IRi4CZIuZj\n" +
              "HiJr/gArcYE41faJZ95QDYfwPEQ8WA6YTQQq+xqMKft248i6xwdMA5W5D2EJaIUP\n" +
              "r4/B7uCshq0Yh1JIZXxwa1YZowBzJ2gzA/nSZQ2LvnH+iJqjIKlwh6amDj/vwIqg\n" +
              "RHfJLqSNQ/HV99334gC5WFhJo6LcX4wvD1b6UoTfe0yTviJVGVhqkm7n99Pc+o73\n" +
              "avCgXv3BgaVuwjZoX6qhmJKz8BIeARI4dT2DJbT6V207p4E/rBOwsvQAh3ZbcszG\n" +
              "quqBuccJaxbM319l/NoSTuDA58chUmRcMayZE7kwf5pSQjboloUZaEJ20/KsGjo0\n" +
              "mjH6oK/f6AGAPUzYZAqiCgRW7gDbKVGQUZX495LhhG+nwNwdV7UlQzX/4wARAQAB\n" +
              "zSJzZXJ2ZXIgdXNlciA8YWdiYXJhbmF0aEBnbWFpbC5jb20+wsF6BBMBCgAkBQJk\n" +
              "9w6/AhsvAwsJBwMVCggCHgECF4ADFgIBAhkBBQkAAAAAAAoJEIi+SQm73PwLStAQ\n" +
              "ANEy6ZU4AXnh9Hys7r9AtiCklEkJ7XK75/MlVj9uZOOyYtb5q5FsiLTrJ3Om283F\n" +
              "zYYtly7+SfuAKoSCbB1C971BEXSFL8CTrP0ckwzd6v7A7CQD7fV8d362D+1b+xPa\n" +
              "awP7mzjcHwufC3ZnFaAiir07IgN6EXQGD7FGzg9feylAgf7aLvgLDLSaDAuHtu8w\n" +
              "B1HkORBf3NzG4CpGRGusANURzGVR152qP+2SDiV2ILQwfE0mJ3/KjFYxFemMChQc\n" +
              "HSvWUI+5JvyaiXBfP0LQMygXzSZbpjxKf3SG/a/x4bGiqVcGIV3Xtc9MLTBPDbjy\n" +
              "J9/BMhh/JH7YlsII7vb+2in+z3KpeOuSPkslqu4OQEyG/XIE1UzHfavNWVs+hx+A\n" +
              "9jrVDqEC0CPEKQFyyt2XZcXfU5cled5b550+IsetMKzAUEZu0aujOOZ+QPFpFUn4\n" +
              "BEkMo4osMqc3EbYLHcAUkuqP18LgDtjzJn/6FV139kzCrLCS+Y18fHKixa0ceCJO\n" +
              "W84zpGbgRAnmtEhLJma8BoQMM3GblAtP76H6XmKTQ/sTx6B5irFAJP+3CdwJQvYM\n" +
              "S4jVuSqVajO9BL6NuK98dPWb9Rb6Oi7+IlBxSriZX8cFvc2az8miiHWLkgtpXfU4\n" +
              "p5SY0r+3jBaqrtz/90Si27EGwJvFErlnvFONSvWslj4FzsFNBGT3Dr8BEADCrnKa\n" +
              "XYuVVNoub63s3923C3XpzcNY98uBLUgAJJjBThi1Vdo2OE4Pk8gKCtN9S+ctmMd5\n" +
              "c/2jJ3keliq6oBy68MCaPVbJnESh7fyfCFpoZy5NrlOU7xwDCfHO9PB3GlbstJYU\n" +
              "oT2TWOnIgaCOdj0W5rqgo+gN4/y9tEYE77u6cLSGaEepseX+o4tJp9Hd4AonV1QJ\n" +
              "lqyxNzcnPEFkVzg7uYm0z+bg7+HJ5ICsY5ulnbgpbwf0BQ07BKsWX+FrOUm+aUeh\n" +
              "Z7W9DP3c0xzT8p97p7HDbJPwDzioJgO3osEPy3ON1lu9BpGQVlSAfvATG7wU+9Xa\n" +
              "lwSdQzyVnamk55M71L74Wza+4V2cFhUy8xxQPCIIkxCTw5cebvMS6MGS+89ilBQM\n" +
              "axnCYQMPPjK12vmfTwOy1A59Tty4cp5Wyf09Q6zc8Jpao8wM5+3PDWph+9uHaxbd\n" +
              "AcJV/sbzVpyN3AltcPR8Lz5z54+HkNKwVQqg+M8bfZiMw0zoQLT7nrKkNsm6JOxN\n" +
              "qKEsiuGTwiDQQscYCcxtGFsTJVlCXfILpgmXCkEKQrl+dI4cX5EogJ1U0+ooiTSi\n" +
              "Vtdoaq2JWONGugwseQNEH1Q+N+ugg5pNR5OKHMKOb1JuNNOOx8T8GwdnZ53lev6b\n" +
              "0dDB2PgHoT2X5D5i09H2VW+bdrWxeFpMvfUzDwARAQABwsOEBBgBCgAPBQJk9w6/\n" +
              "BQkAAAAAAhsuAikJEIi+SQm73PwLwV0gBBkBCgAGBQJk9w6/AAoJEK3S/98x3Ddk\n" +
              "4tIQAI5BTmjwp4YwwRRznvjIZKOCn0b0bizXYo+hIRyusA26mrzBOaAwF3pbLnlO\n" +
              "kSPoujJ6Ee04vfB4E78e90gAzCRJlbW9uxrih2zft4hsuBaUUmvSVS2gODF7NOxH\n" +
              "lH4Gq66bfzKmMIIHz0SYxivacC0U540NnPXG05KSMkKCE+FTfLQ2w7Yrji0B6FC4\n" +
              "30/CdWc5UyIHDiG+MW8wYbDlBdBHcURKsGyqxedGQUk5Q+5NcV6W835UHkHr6BRh\n" +
              "ldeTLiUG0Lm3CUWcgo1QDzT8JPaqO3y6tLGsShgFskiIfSccAWc24q3gbmvYP6pB\n" +
              "PdItZZMw2XDgs97xaq+Fkmq2iZoxkiDViSR4om9HDGXh8nc+s0/R/kFoIid2qHDO\n" +
              "135iW8gC0qgmljipY4UOB73XU3RIEy32PvvBTSjf309DkvrSKTyKJ0yCHijXjyMu\n" +
              "Yz0SRxFZFCW3lnBbo4f1lcJcNVjor6fXanJiBD0R6kj8wcn9zr5NzpI64+/kmUA3\n" +
              "4UuGm1XRJ9vIfKFqgwsNm8e9h6cs1lnEt+T5GyiBPjY7m+fYSsFP+xbtMuI/ZV1/\n" +
              "gJgqlYhPylWY/hcYQN2xOpIp9ceMkrR6QfInZE9NspXfcF6CSGKq9TeBQgpGartm\n" +
              "VvH2QnsIDW0p0sEYJ2hL1itd/mqLj0s/WyMJoLLFEUGVBKLzZX0QAI66W+5G6Fb8\n" +
              "/KUOrkAMYw6wsSXrgmCflxGByCXJL1n1JxWGofYHypkPuesyIepMkYgBKhfMX/TW\n" +
              "NT6Fk/dF/Bdi32pMmfyO7C7FQUyb9tfn2fCOt+INGTflnVUb3p/IB2GBcna9CoiF\n" +
              "VzDhmdMFyoSyW2daxgWTZfIFaAmy0N4ZwcycaGxebMwFLIEOEhA3NjkG5INvxvOd\n" +
              "wWRe/AzSO73XzrW/xvxuAFnn0Qg9XXmN1y8Br+bS2tKMudMqozhMp8gBmVNp2Aos\n" +
              "YUNFnXiJKOM4noaC7puiwUapBAkOTFm6k95Qr7lJ92qGOL0f4rswASMoXyCnBkED\n" +
              "SZ2jaTH5dHKkNSjhGuxoHWSlgDNAmOyRr5waRco1rwklYrL5ETw6pIk72EnlwqNF\n" +
              "j8Iu8JlBA+3aot8Chsbcz1WcHtl5P8hItOgXXEdGHpZ7rqE4VewB7JAtEioqzWHL\n" +
              "AzAd8JfopNjzF88dukYIODDk6XMulN8GqAh89dB04GGeYR+hP5GExnEyNfDGo/Qk\n" +
              "KFqxWgjpZ6/Pguf2SzJGDvX6eDTcMKUty4wJvK6NSFASDrq5qT1J+y20E8qqg18Z\n" +
              "/qe/ajT7Ygp/d3u1eBCkUKgFymEyPWJLS3cg/tfdYTqlTCQRBfWnBY7M8OO4tO5g\n" +
              "wh6cYMOowXxcRC4TW+HZa5rxuaoKEmodzsFNBGT3Dr8BEAC6L1mr9vTqJ++01koC\n" +
              "ZWhS+0WG4LHMddsvBygs47Z8ZMY7yw4djFHVwoDUkbpMBjmkNdkAYKA5bYKorFDJ\n" +
              "ISYUTlFmKj1TMpYw6OmkaxAQabhdFb1/wKmevK1Vz5SyJ4FkagtEeghnEvio5qfH\n" +
              "OBOCWwvDtBg/hVrL8rLueCIf2eipk5H/Ycn6eQ0C+PUac1ztAxgy0CyBSgx4znVH\n" +
              "zalrD8fbdbqt+B37KZCQ0WDVhvoNQyfmmjIGoB5+UZ3GFbDexofZkp0jjnb2/ao4\n" +
              "ZFlzFvAqvM1xJtVw/uVUdxYQtyzpcWS+Xn251KFplzEIAOPVhEKUxBYEjr45bWFC\n" +
              "EXD7dDRQw8eJERfKWm1MfJmM6VUbsflvRWPtdLOFr/U4Pg+Q5T3Cn7DpYQa3qhvT\n" +
              "/9L6hOmDkbfn5ulTZiWKNnIZO191ZLjp62y6GTinCDZaRGRsIXiYYIxIeJi4UNz4\n" +
              "cEp7EOAGd3qw4Wv1K0SjhX1idD+KdrZ463RN1oFnC8AnxMy4BrMYmwSlfhtrH4qr\n" +
              "ELVjDj9zB3xdgrVegIy4ClrTK1IwtlV0yxCBgvszwYBunbcDo9YZbvyb2bwIW17d\n" +
              "riu8eiDlb/1Ytcp4VZopIOgbsm8CI5HO2dfCI/mIJ7SXZKZS3/tWrbcgqyrhuM1S\n" +
              "dmqktjs09OcjtFf6sG0Wzm44nwARAQABwsOEBBgBCgAPBQJk9w6/BQkAAAAAAhsu\n" +
              "AikJEIi+SQm73PwLwV0gBBkBCgAGBQJk9w6/AAoJEEdiHFUpsf3FkAcP/j0IkGbT\n" +
              "Lvh1mPk7SGgjvpAa3KZYBYA7ugaIQtVnxNCLkoIKaIY3OZ1OmyZUvy/sZpyQEi1/\n" +
              "XOBuVIQzYjM3eJiQ5REUsddhyice0SNfdvrqcNbXAxmF6FNOHf2bvHYPYnHEF9xE\n" +
              "JDT/iXFv6mxf5dvyGUe/bqZTLUtSO22VyyezBaVvUdfR8mrVXcbfNmTMoNIox5DE\n" +
              "wdCIEMZraE6zEj/TZ4LdlvTJxadZEO+x8MklnSKKp8OQT63uqSWp0DRJpDLMJ/Nf\n" +
              "I2fvCoxk82nuuPh46LP0xKJ4vmBHGxPGQcapbjHUxiLqImR+R0XSGT6c0WtLp+C2\n" +
              "q3iJomiFFYHEg5CtMS0snlpl7r18hOc/SYQ6lZ05l6AvNfUalPnrZBtHwP1ysFk2\n" +
              "3HGm7E65+F4KRodM6eF+5SY3RxVHLva/K0xuvokv/PYRzD9/iIRtHxJknOfe1Wyn\n" +
              "R09FsexnPFD/6ry+lptPlr8+yIe0OtWMG+TsXQj1eTfPeaRGB1gnw1RXcXcKJ5DY\n" +
              "6DCdvNAHfiLSKnpKlA376dvacWKoC0fpxUyFISogjxmvZTGlPhh2JytklsaXVUWZ\n" +
              "HtNtuHp6qEzqApy4LvhWu65jDzRKBySTVPtEFHyLec+q6MyRWqE7huJBnS6/5168\n" +
              "RivuMULPQ9mYGhdi/chjprxufScmOWafpAT8h3MP/R/TXxyVOklDjamk6WRC9dGV\n" +
              "y8Tytny6/p5itKSxNETlhWvcCN7sXJfHHId+KwHMW7crZ/y7bgFKVjZE9Pg9M0Dl\n" +
              "gIpQbSCOyZFnjTKZFz0ni0f5k+K+HA6frUFoYmoFLx0fXxCClsG0iw7qjKcRDBBC\n" +
              "FiJ0yLW4NzhubrsJvLY8Ya55xsI/IpYPneBtfafHai8StQwQi9YYNF/HTCuGGPqI\n" +
              "22dWJQNANZRKAZoM3sb6SwA11Ms3Ff57bbnMB9rc6+Wecx6YAcOpObaRbcHHEZZZ\n" +
              "djV8/1qtwUFOUKMplTIkke5TTr5dJC02j8JDt8cZ5LmBLNqTuUHFqQcZB7eMpNGr\n" +
              "lUkaqW0EvwvYYS3zxGUtv5HKjaqMXdfOmPMndwaS713C6XyHqiCxE/55d/tFWd27\n" +
              "LLVrnKmCCdhnUkriG2bgazFQiDJ0DHnAUdCEwOFzS5zSx87byhSQ98guJYRo8jIQ\n" +
              "HYDeu5YMqUf8YFCRIuS1i0BHbK0i8RF7Gx3wnQHnihvlrn+6EC3DdscpQDwn3N32\n" +
              "W6N8o85r4r0aYiXP/USxfmudzzEL6w6PNRNFvPjyaNkQD4EAGmK2um9ZZz2aRyrL\n" +
              "d0KkjF8Hz+S2LF8oEIw8UEpf2t9bDSSIkJfpNbW3mZ29gnXVu4eSUDh4QLMdZGPA\n" +
              "MRgtg950cPd0FXpE2jWv\n" +
              "=y7yk\n" +
              "-----END PGP PUBLIC KEY BLOCK-----\n");

      String message = "{\"userId\": \"ugo\",\n" +
              "\"password\": \"ugopassword123==\" ,\n" +
              "\"ip\":\"192.168.21.1\",\n" +
              "\"linkId\": \"\"\n" +
              "}";

        System.out.println("==============encrypt message");
        String encrypted = encrypt( message,  publicKeyArmored);

       System.out.println("==================== the encrypte string is: "+encrypted);
        System.out.println(encrypted);

        /////////////////////////////////////////////////////// pgp key decryption
        String privateKey = "-----BEGIN PGP PRIVATE KEY BLOCK-----\n" +
                " \n" +
                "lQPGBGhilwgBCADVuXO0tV3FyvNRj/PEtaTlIvNkAQ+TrxIb8J44PGxqWoiLOGq3\n" +
                "F3PA8aUiEq8WNCd+xzATk44xqu9JcuMiEfsm3JiZo1VU/GE4k621ZwK/qRndTWW3\n" +
                "CxZGgRsOdogC3wTsdbXejNX+1F7+d525qjVo+U1aUb1KZ0wLP3AL0i1ztL9iHcLG\n" +
                "He0uNMq42tbxTztlR99O5JnjpiE5UrZXVebfAVJFdHJbbqzPRgnvOle5EXZvH8ei\n" +
                "4TFrPRyhP+l4r3kjBYfXAhQjc5jfiZaudfZsZ+jWAT2M5ZSkCVCnPf9Jd1XFTH+G\n" +
                "yRviD8ll3ct8Lo4uQKbxzU+cWqQVSaSpgVL3ABEBAAH+BwMCjvaCRBO9O63/uHpO\n" +
                "dgzimuMe922xTUC2BXxVav9X6fScpKJ2q+ekFH9KBhjBfiLXaG1HdSR3kKYiaZ+y\n" +
                "i/n0Gu2FCiSs1U7jO4z54IGIDt+i1+rRA/t062OtL2W0SbBftp1niZTpav1xXzHx\n" +
                "wU8we75Aq3nl3nWxHzdErXNTH0BUSXwMO8KmVAnQtg1shtAPMZtmMXhasZ+In15o\n" +
                "nm7BRjOHOZksO1NsfcnQ+DSxxiVsgKyb1QPuzOR6lJ8OFPlx4YGiLVKfd0VbV30i\n" +
                "WvsQ3H6TPZRGbnXayG3Kalck87IDcC+NVapzChhzTd05w3qyxFSHZUjmZlsc0Owc\n" +
                "lJQs3dRPnV0kvG5vXB2lHdmDvuF4ZQxjCLMsYdZouZDWeWWmY3ANIJ3fLz9UkRlA\n" +
                "0GWplAExWXHgxHvrOyjYTZPTg+EpvadZrGaMWBcbhTR0xvUGLTeuaic7DjvRJ2ok\n" +
                "7+2EixJcULQc2+X886zvzN7Z2Jhc8N5hxNywk1OGvu+GaAeyfhfPIGwtVgFzh+Hd\n" +
                "BtBp6ZrSe9mZXW7iFIO5ExzJMTi7+cL6HI5jA+9V+uTbik3N83fVDdGG3X9WOcR+\n" +
                "Qj4zOtXjLiaHyOlOEcnhDPRc5JexLu5/0WfON2ihC5KTLwu0MH5CVSEtKSzVxXFz\n" +
                "RIWGiRkpvTvD73iljJ27yMd7twYuENTygFKctuBJIMcD2PuWpmvi1rx2aCKCzkGf\n" +
                "6to3lvjZAFybzr9fxC8u8W+5z6KYpxFmwPUtQVK03QNDBHc+Sg5MSc/jWtrygMSn\n" +
                "iu3MHPmN+P6AqozDjpCJPhfxXKDjXmNBQpF3v3C89rYSU0xuYQ28vyyP1F64LwOT\n" +
                "hWOnqmbghCUQ0KG/9H5aG2dGOqjBUICwWfQrIF7aGf9h0Xk/TyuUlnNy4Zqyeirv\n" +
                "toC4zuW3LrlOtC9BZGVwb2p1IEFkZXNoaW5hIDxhZGVwb2p1YWRlc2hpbmEwMzZA\n" +
                "Z21haWwuY29tPokBUQQTAQgAOxYhBI6aNnzesCtFqp8eiUUiIqOBJUknBQJoYpcI\n" +
                "AhsDBQsJCAcCAiICBhUKCQgLAgQWAgMBAh4HAheAAAoJEEUiIqOBJUkn6RsH/jAi\n" +
                "AnPUx+4TZlzqSEP9RKyqdD/+HnLj+xIdbA1y3nSf8ucyidgj8Y2CHnJmCgTtRfbs\n" +
                "5MnOpX0Gbx7rGAAhh/V8Yycp7h6X8s+IkGRhMh/zThcrfiJbFMxkSXsAwda4a0ga\n" +
                "FG9qphV9+aVz8VfrMcrBlMkqYGdX5CaQEseZ94HxutyANr1R9XkCBhd26duDT2wo\n" +
                "hGkvlm2L+EgsHiA6Mtq2YXIhaeGGhiCqFunLkvCnhuuNp6QmANWHbJqX9Wp/IGA5\n" +
                "v9yJvwnJu1ieBSf7viELnZzrgimJuEv3+24rBilwjbffc8oA8TSbkQmwLM/VWBDO\n" +
                "D3ysZplqJtRGhHVPbn2dA8YEaGKXCAEIAMSbh4YdRKsUBAyl9AgfoTzvMbdUaQfJ\n" +
                "j6o4V0XaGxMd/7ADWyyIKg3mCkBZYTfPnkIrHVjd1wTBcy4KypC2hxhELsoBWOa1\n" +
                "MW9tW3rF8O2RgS4ZO/KSkY2j0SzE4Nq3/QUJVfsx8NZuWk/iDsalWnO07+G4pW9c\n" +
                "sXAFBVGm+KyEt0Omg40HJhflncyvtQeyVMiwCBXu7zkD1zMnn95OWQxuPaq8kv9T\n" +
                "e3RlmbSZZK31ubwsEkvAKEMy6oKP/3Nb7KQmwVGoTbj1aT7aRKNeKUH5/nEG+YsR\n" +
                "xWfpuIpWFI1w4yNa8P5rR3OA4i6O+kw1/Wl7YC5jqlY3+pBk3CIhv58AEQEAAf4H\n" +
                "AwLNbFCliTFHU/9M4cEF04WU1uKFZynYxKmXJjZ6YAmppSTR71ok7dfb6GyDXATI\n" +
                "9XEHe5rsbOc9atkBez/6Dp5mizsV8qToqwLrJgMWFS6jGFJO2Duo+pvD3/Y23v76\n" +
                "BaZgeTzmHDCmyD0pFGYmrMt8AqiEIk/nOt6e0K/0eNamSy5HcSmTZnGCHmp4t8Ep\n" +
                "/G6V+OQtj0VW4lVEjLWVCAyK3ntXARWatycrqDsyoXpKDGIcTXGwkOTvmaiAR776\n" +
                "JOJKS0/+KbazJ2XFSxZwH0aaoIBtIheHr6HRYI0jywh1insIukmyyWXkR+obQErb\n" +
                "XPx8QyQuTyVArw2f/80oM3N9gIQNODr50uDVhMGDP5aC1QapGuIxHgpk6ane163h\n" +
                "qOdyoz4AiTxIk1M+grvikUyIezpLo4T5ysZaGLlK2AnE8IETHsDUe0uC1KHraeg/\n" +
                "lmX+O9XNSOHmLPen79AgLc9yxrVmCOTH3kaunZcgXJTlYpluBrpv2KjNyDazMA6t\n" +
                "JmZN/JmwjBPV/1KAP5rzeDm/I9qDnlPjrdEpD+FHvSDeXrH6ZSu9DDe3uqgWynK6\n" +
                "7xtg1wSnbHjXeNSyqFrJU+qxTSOxi7DpKo50EQNUOk9QyubLfHzqO3c+1Bi5kN24\n" +
                "dv0P+vWPS6+xPAgP42Jr0zi0PHWgit2lkqsoNua06+DCBNFuQKGyaAe9W/0aTTnS\n" +
                "KXuFtQAXOUpBIwMoEvBuSjGX/R2XKukByvNjfdyXb8MANmJ01rRPY6O7KX7/JYgw\n" +
                "6SIg5aFuCpuTbXijKvRBV4XCAXH0XXPu6yH1azn0aN2FrEhYltKY0QdyskD1oKrf\n" +
                "yRD8onb+8MD2SFsXL+KS34fiKJTu7Xkob/mmawgwMWKRxO0p75acZ0+AZ5Sr4bTw\n" +
                "I8xRly1gm9zSqGsNW7LELJWdSjLV/z+JATYEGAEIACAWIQSOmjZ83rArRaqfHolF\n" +
                "IiKjgSVJJwUCaGKXCAIbDAAKCRBFIiKjgSVJJ352B/4/por9wWLADexWq41u8B9A\n" +
                "jyJy0dYkvm3a6C9E/z+HZNOlB8arvxlX7D2/FJURMBvY9NnY/6YwstLkcwErfTic\n" +
                "NxAY2/BwhIuhVJatYd4ah89ycqnn/XD6E/Pt/fPj48f2aRMeXu+Bm5Al3V9uN0GV\n" +
                "UzTS6hn3pA14Q6y41bWwmxq/n74VGpmT3Ymur5SGsQ4hzryl+eSSb9WeLo2iq3xJ\n" +
                "662tLYZo1u9SxbQcnTXqrq6lfY6uNjenCm112Fhnb61NSnkCoorj4r18VDGvpZkG\n" +
                "tTsrzGyq7P0J6vs8Tprql8DJT/m2ZfaWDJoo5aud+SWXWNlB4RnGCzkPZz11AOu/\n" +
                "=PAPu\n" +
                "-----END PGP PRIVATE KEY BLOCK-----";

        System.out.println("========================================================== decrypting String");
        PGPPrivateKey pgpPrivateKey = extractPrivateKey( privateKey,passPhrase );
        String decryptedString  = decrypt( encrypted,  pgpPrivateKey);
        System.out.println(decryptedString);
    }


    public static PGPPublicKey loadPublicKeyFromString(String publicKeyArmored) throws Exception {
        InputStream keyIn = new ByteArrayInputStream(publicKeyArmored.getBytes("UTF-8"));
        InputStream decodedInput = PGPUtil.getDecoderStream(keyIn);

        PGPPublicKeyRingCollection keyRings = new PGPPublicKeyRingCollection(decodedInput, new JcaKeyFingerprintCalculator());

        for (PGPPublicKeyRing keyRing : keyRings) {
            for (PGPPublicKey key : keyRing) {
                if (key.isEncryptionKey()) {
                    return key;
                }
            }
        }

        throw new IllegalArgumentException("No encryption key found in public key block.");
    }

    public static String encrypt(String message, PGPPublicKey publicKey) throws Exception {
        byte[] messageBytes = message.getBytes("UTF-8");
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Armor the output to make it ASCII
        OutputStream armoredOut = new ArmoredOutputStream(out);

        // Setup the encryptor using CAST5 or AES256
        PGPEncryptedDataGenerator encryptor = new PGPEncryptedDataGenerator(
                new JcePGPDataEncryptorBuilder(PGPEncryptedData.CAST5) // or AES_256
                        .setWithIntegrityPacket(true)
                        .setSecureRandom(new SecureRandom())
                        .setProvider("BC")
        );

        // Add public key method
        encryptor.addMethod(
                new JcePublicKeyKeyEncryptionMethodGenerator(publicKey)
                        .setProvider("BC")
                        .setSecureRandom(new SecureRandom())
        );

        // Open encrypted stream and write message
        try (OutputStream encryptedOut = encryptor.open(armoredOut, messageBytes.length)) {
            encryptedOut.write(messageBytes);
        }

        armoredOut.close();

        return out.toString("UTF-8");
    }

    public static PGPPrivateKey extractPrivateKey(String privateKeyArmored, String passphrase) throws Exception {
        InputStream keyIn = new ByteArrayInputStream(privateKeyArmored.getBytes("UTF-8"));
        InputStream decoderStream = PGPUtil.getDecoderStream(keyIn);

        PGPSecretKeyRingCollection secretKeyRingCollection = new PGPSecretKeyRingCollection(
                decoderStream, new JcaKeyFingerprintCalculator());

        for (PGPSecretKeyRing keyRing : secretKeyRingCollection) {
            for (PGPSecretKey secretKey : keyRing) {
                if (secretKey.isSigningKey() || secretKey.isMasterKey()) {
                    // Extract the private key using passphrase
                    char[] passPhrase = "EES13140036@oou".toCharArray();
                    return secretKey.extractPrivateKey(
                            new JcePBESecretKeyDecryptorBuilder()
                                    .setProvider("BC")
                                    .build(passPhrase)
                    );
                }
            }
        }

        throw new IllegalArgumentException("No suitable private key found in provided string.");
    }

    public static String decrypt(String armoredEncryptedText, PGPPrivateKey privateKey) throws Exception {
        InputStream in = new ByteArrayInputStream(armoredEncryptedText.getBytes("UTF-8"));
        in = PGPUtil.getDecoderStream(in);

        PGPObjectFactory pgpFactory = new PGPObjectFactory(in, new JcaKeyFingerprintCalculator());
        Object object = pgpFactory.nextObject();

        if (!(object instanceof PGPEncryptedDataList)) {
            object = pgpFactory.nextObject();
        }

        PGPEncryptedDataList encList = (PGPEncryptedDataList) object;
        PGPPublicKeyEncryptedData encryptedData = null;

        Iterator<PGPEncryptedData> it = encList.getEncryptedDataObjects();
        while (it.hasNext()) {
            PGPPublicKeyEncryptedData pked = (PGPPublicKeyEncryptedData) it.next();
            if (pked.getKeyID() == privateKey.getKeyID()) {
                encryptedData = pked;
                break;
            }
        }

        if (encryptedData == null) {
            throw new IllegalArgumentException("Encrypted data not intended for the given private key.");
        }

        InputStream clearData = encryptedData.getDataStream(
                new JcePublicKeyDataDecryptorFactoryBuilder()
                        .setProvider("BC")
                        .build(privateKey)
        );

        PGPObjectFactory plainFact = new PGPObjectFactory(clearData, new JcaKeyFingerprintCalculator());
        Object message = plainFact.nextObject();

        if (message instanceof PGPCompressedData) {
            PGPCompressedData compressedData = (PGPCompressedData) message;
            plainFact = new PGPObjectFactory(compressedData.getDataStream(), new JcaKeyFingerprintCalculator());
            message = plainFact.nextObject();
        }

        if (message instanceof PGPLiteralData) {
            PGPLiteralData literalData = (PGPLiteralData) message;
            InputStream literalIn = literalData.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            int ch;
            while ((ch = literalIn.read()) >= 0) {
                out.write(ch);
            }
            return new String(out.toByteArray(), "UTF-8");
        }

        throw new PGPException("Unexpected PGP content.");
    }
}