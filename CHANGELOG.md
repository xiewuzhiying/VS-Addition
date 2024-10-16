# Change Log
## Common
- Fixed MixinStickerBlockEntity fails on server environments [#40](https://github.com/xiewuzhiying/VS-Addition/issues/40) (by [fxshlein](https://github.com/xiewuzhiying/VS-Addition/pull/41))
- Added new method `fakePlayerKill()` to ShipHelmPeripheral (Temporary solution for [#42](https://github.com/xiewuzhiying/VS-Addition/issues/42))
- Encased fan mixins are now disabled by default
- Changed Cannon Peripherals to generic peripheral implementation (to coexist with Inventory peripherals)
- Added placement helper support for Clockwork's Flap block and Wing block
- Fixed where the wings on the contraption had to be reassembled after re-entering the world for the contraption to work (such as Flap Bearing)
- Slightly changed the mixins of minimap mods (actual effect unchanged)
- Rolled back Mixin Extras version to be compatible with Axiom
## Fabric
- Added compatibility with Fabric version of Net Music
## Forge
- Fixed CBC Modern Warfare rockets not translating to the world when fired from a ship.
- Improve compatibility with Tallyho (maybe still have issues)