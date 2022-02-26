#include <cstring>
#include "MonoString.h"

void MonoString::setMonoString(const char *s) {
    string str(s);
    length = strlen(s);
    u16string basicString = utf8_to_utf16le(str);
    const char16_t *cStr = basicString.c_str();
    memcpy(getChars(), cStr, getLength() * 2);
}

void MonoString::setMonoString(string s) {
    length = s.length();
    u16string basicString = utf8_to_utf16le(s);
    const char16_t *str = basicString.c_str();
    memcpy(getChars(), str, getLength() * 2);
}

const char *MonoString::toChars() {
    u16string ss((char16_t *) getChars(), 0, getLength());
    string str = utf16le_to_utf8(ss);
    return str.c_str();
}

string MonoString::toString() {
    u16string ss((char16_t *) getChars(), 0, getLength());
    string str = utf16le_to_utf8(ss);
    return str;
}


static inline uint16_t byteswap_ushort(uint16_t number) {
#if defined(_MSC_VER) && _MSC_VER > 1310
    return _byteswap_ushort(number);
#elif defined(__GNUC__)
    return __builtin_bswap16(number);
#else
    return (number >> 8) | (number << 8);
#endif
}


std::string utf16_to_utf8(const std::u16string &u16str) {
    if (u16str.empty()) { return std::string(); }
    //Byte Order Mark
    char16_t bom = u16str[0];
    switch (bom) {
        case 0xFEFF:    //Little Endian
            return utf16le_to_utf8(u16str);
            break;
        case 0xFFFE:    //Big Endian
            return utf16be_to_utf8(u16str);
            break;
        default:
            return std::string();
    }
}


std::string utf16le_to_utf8(const std::u16string &u16str) {
    if (u16str.empty()) { return std::string(); }
    const char16_t *p = u16str.data();
    std::u16string::size_type len = u16str.length();
    if (p[0] == 0xFEFF) {
        p += 1;
        len -= 1;
    }

    std::string u8str;
    u8str.reserve(len * 3);

    char16_t u16char;
    for (std::u16string::size_type i = 0; i < len; ++i) {
        u16char = p[i];

        if (u16char < 0x0080) {
            // u16char <= 0x007f
            // U- 0000 0000 ~ 0000 07ff : 0xxx xxxx
            u8str.push_back((char) (u16char & 0x00FF));  // 取低8bit
            continue;
        }
        if (u16char >= 0x0080 && u16char <= 0x07FF) {
            // * U-00000080 - U-000007FF:  110xxxxx 10xxxxxx
            u8str.push_back((char) (((u16char >> 6) & 0x1F) | 0xC0));
            u8str.push_back((char) ((u16char & 0x3F) | 0x80));
            continue;
        }
        if (u16char >= 0xD800 && u16char <= 0xDBFF) {
            // * U-00010000 - U-001FFFFF: 1111 0xxx 10xxxxxx 10xxxxxx 10xxxxxx
            uint32_t highSur = u16char;
            uint32_t lowSur = p[++i];
            uint32_t codePoint = highSur - 0xD800;
            codePoint <<= 10;
            codePoint |= lowSur - 0xDC00;
            codePoint += 0x10000;
            u8str.push_back((char) ((codePoint >> 18) | 0xF0));
            u8str.push_back((char) (((codePoint >> 12) & 0x3F) | 0x80));
            u8str.push_back((char) (((codePoint >> 06) & 0x3F) | 0x80));
            u8str.push_back((char) ((codePoint & 0x3F) | 0x80));
            continue;
        }
        {
            // * U-0000E000 - U-0000FFFF:  1110xxxx 10xxxxxx 10xxxxxx
            u8str.push_back((char) (((u16char >> 12) & 0x0F) | 0xE0));
            u8str.push_back((char) (((u16char >> 6) & 0x3F) | 0x80));
            u8str.push_back((char) ((u16char & 0x3F) | 0x80));
            continue;
        }
    }

    return u8str;
}


std::string utf16be_to_utf8(const std::u16string &u16str) {
    if (u16str.empty()) { return std::string(); }
    const char16_t *p = u16str.data();
    std::u16string::size_type len = u16str.length();
    if (p[0] == 0xFEFF) {
        p += 1;
        len -= 1;
    }


    std::string u8str;
    u8str.reserve(len * 2);
    char16_t u16char;
    for (std::u16string::size_type i = 0; i < len; ++i) {
        u16char = p[i];
        u16char = byteswap_ushort(u16char);
        if (u16char < 0x0080) {
            // u16char <= 0x007f
            // U- 0000 0000 ~ 0000 07ff : 0xxx xxxx
            u8str.push_back((char) (u16char & 0x00FF));
            continue;
        }
        if (u16char >= 0x0080 && u16char <= 0x07FF) {
            // * U-00000080 - U-000007FF:  110xxxxx 10xxxxxx
            u8str.push_back((char) (((u16char >> 6) & 0x1F) | 0xC0));
            u8str.push_back((char) ((u16char & 0x3F) | 0x80));
            continue;
        }
        if (u16char >= 0xD800 && u16char <= 0xDBFF) {
            // * U-00010000 - U-001FFFFF: 1111 0xxx 10xxxxxx 10xxxxxx 10xxxxxx
            uint32_t highSur = u16char;
            uint32_t lowSur = byteswap_ushort(p[++i]);
            uint32_t codePoint = highSur - 0xD800;
            codePoint <<= 10;
            codePoint |= lowSur - 0xDC00;
            codePoint += 0x10000;
            u8str.push_back((char) ((codePoint >> 18) | 0xF0));
            u8str.push_back((char) (((codePoint >> 12) & 0x3F) | 0x80));
            u8str.push_back((char) (((codePoint >> 06) & 0x3F) | 0x80));
            u8str.push_back((char) ((codePoint & 0x3F) | 0x80));
            continue;
        }
        {
            // * U-0000E000 - U-0000FFFF:  1110xxxx 10xxxxxx 10xxxxxx
            u8str.push_back((char) (((u16char >> 12) & 0x0F) | 0xE0));
            u8str.push_back((char) (((u16char >> 6) & 0x3F) | 0x80));
            u8str.push_back((char) ((u16char & 0x3F) | 0x80));
            continue;
        }
    }
    return u8str;
}


std::u16string utf8_to_utf16le(const std::string &u8str, bool addbom, bool *ok) {
    std::u16string u16str;
    u16str.reserve(u8str.size());
    if (addbom) {
        u16str.push_back(0xFEFF);   //bom
    }
    std::string::size_type len = u8str.length();

    const unsigned char *p = (unsigned char *) (u8str.data());
    if (len > 3 && p[0] == 0xEF && p[1] == 0xBB && p[2] == 0xBF) {
        p += 3;
        len -= 3;
    }

    bool is_ok = true;
    for (std::string::size_type i = 0; i < len; ++i) {
        uint32_t ch = p[i];
        if ((ch & 0x80) == 0) {
            u16str.push_back((char16_t) ch);
            continue;
        }
        switch (ch & 0xF0) {
            case 0xF0:
            {
                uint32_t c2 = p[++i];
                uint32_t c3 = p[++i];
                uint32_t c4 = p[++i];
                uint32_t codePoint =
                        ((ch & 0x07U) << 18) | ((c2 & 0x3FU) << 12) | ((c3 & 0x3FU) << 6) |
                        (c4 & 0x3FU);
                if (codePoint >= 0x10000) {
                    codePoint -= 0x10000;
                    u16str.push_back((char16_t) ((codePoint >> 10) | 0xD800U));
                    u16str.push_back((char16_t) ((codePoint & 0x03FFU) | 0xDC00U));
                } else {
                    u16str.push_back((char16_t) codePoint);
                }
            }
                break;
            case 0xE0:
            {
                uint32_t c2 = p[++i];
                uint32_t c3 = p[++i];
                uint32_t codePoint = ((ch & 0x0FU) << 12) | ((c2 & 0x3FU) << 6) | (c3 & 0x3FU);
                u16str.push_back((char16_t) codePoint);
            }
                break;
            case 0xD0:
            case 0xC0: {
                uint32_t c2 = p[++i];
                uint32_t codePoint = ((ch & 0x1FU) << 12) | ((c2 & 0x3FU) << 6);
                u16str.push_back((char16_t) codePoint);
            }
                break;
            default:
                is_ok = false;
                break;
        }
    }
    if (ok != NULL) { *ok = is_ok; }

    return u16str;
}


std::u16string utf8_to_utf16be(const std::string &u8str, bool addbom, bool *ok) {
    std::u16string u16str = utf8_to_utf16le(u8str, addbom, ok);
    for (size_t i = 0; i < u16str.size(); ++i) {
        u16str[i] = byteswap_ushort(u16str[i]);
    }
    return u16str;
}
