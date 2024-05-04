#include <iostream>
#include <types.hpp>
#include <vector>
#include <Tree.hpp>
#include <fstream>

extern Tree<ASTNode>* ParseQuery(const std::string&);
extern void SetVerbosity(unsigned int lvl);

int Check(std::string readline)
{
    for(int n=0;n<readline.length();n++)
    {
        if(readline[n]!=9&&readline[n]!=32&&readline[n]!=13)
        {
            return 1;
        }
    }
    return 0;
}
int main(int argc,char** argv) {
    SetVerbosity(2); // 0 = NONE, 1 = ERRORS_ONLY, 2 = ALL

    // 检查命令行参数
    if (argc < 2) {
        std::cout << "Usage: " << argv[0] << " <formula>" << std::endl;
        return 1;
    }

    // 从命令行参数获取公式
    std::string formula = argv[1];

    if (Check(formula)) {
        auto *q = ParseQuery(formula);
        q->PrintTokensAndTypes();
        formula = "";
        std::cout << "true " << std::endl;
        delete q;
    }
    return 0;
}
