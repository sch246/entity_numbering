# Entity Numbering Mod

language: [English](./README_en-US.md)

仓库地址: [https://github.com/sch246/entity_numbering](https://github.com/sch246/entity_numbering)

## 概述

Entity Numbering Mod 是一个 Fabric mod，为 Minecraft 中的实体按种类累加编号，并提供死亡消息广播功能。

## 功能

- 为游戏中的实体添加唯一编号(可关闭)
- 广播实体死亡消息(可关闭)
- 自定义死亡消息广播范围
- 自定义实体名称和编号之间的分隔符
- 为僵尸感染村民作了特别处理

## 已知问题

- 它会影响世界内所有没有名字的生物实体，因为目前我还不知道怎么区分:实体是本来就有的还是新生成的

## 安装

1. 确保你已经安装了 fabric-api 和 Fabric loader，且loader版本>=0.16.0
2. 下载本 mod 的 .jar 文件
3. 将下载的 .jar 文件放入你的 Minecraft `mods` 文件夹中
4. 启动游戏！

## 配置

mod 的配置文件应该位于 `.minecraft/config/entity_numbering.json`。你可以根据需要修改以下设置：

```json
{
  "enableNumbering": true,
  "enableDeathMessages": true,
  "boardcastNeedName": true,
  "boardcastDistance": 128,
  "nameSeparator": " #"
}
```

- `enableNumbering`: 是否启用实体编号功能
- `enableDeathMessages`: 是否启用死亡消息广播
- `boardcastNeedName`: 是否只广播命名实体的死亡消息
- `boardcastDistance`: 死亡消息广播的范围（以方块为单位）
- `nameSeparator`: 实体名称和编号之间的分隔符

作为调试，你可以打开`/saves/<存档名>/data/entity_counter.json`修改每种生物的当前编号计数

## 兼容性

本 mod 兼容 1.21 版本的 Minecraft。

其余版本未经过测试。

## 贡献

欢迎提交 issues 和 pull requests。

## 许可证

本项目采用 [MIT](./LICENSE) 许可证。

## 联系方式

如有任何问题或建议，请联系 QQ 980001119
