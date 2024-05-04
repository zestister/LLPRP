#include <iostream>
#include <string>

extern int scanMyThing(const std::string&);
extern int error_result;

int main(int argc, char** argv) {
    // 检查命令行参数
    if (argc < 2) {
        std::cout << "Usage: " << argv[0] << " <formula>" << std::endl;
        return 1;
    }

    // 从命令行参数获取公式
    std::string formula = argv[1];

    // 扫描公式并输出结果
    if (scanMyThing(formula) == 0) {
        std::cout << "true " << std::endl;
    } else {
        std::cout << "false" << std::endl;
    }

    return 0;
}
