//package io.github.xiewuzhiying.vs_addition.compats.computercraft.apis;
//
//import dan200.computercraft.api.lua.ILuaAPI;
//import dan200.computercraft.api.lua.LuaFunction;
//
//import java.io.UnsupportedEncodingException;
//import java.util.zip.DataFormatException;
//import java.util.zip.Deflater;
//import java.util.zip.Inflater;
//
//public class DeflateAPI implements ILuaAPI {
//    @Override
//    public String[] getNames() {
//        return new String[] {"deflate"};
//    }
//
//    @LuaFunction
//    public final Object compression(String input) throws UnsupportedEncodingException {
//        byte[] data = input.getBytes("UTF-8");
//
//        // Compress the bytes
//        byte[] output = new byte[100];
//        Deflater compresser = new Deflater();
//        compresser.setInput(data);
//        compresser.finish();
//        int compressedDataLength = compresser.deflate(output);
//        compresser.end();
//        return output;
//    }
//
//    @LuaFunction
//    public final Object decompression(String input, Double compressedDataLength) throws DataFormatException {
//        Inflater decompresser = new Inflater();
//        decompresser.setInput(input.getBytes(), 0, (int) Math.floor(compressedDataLength));
//        byte[] result = new byte[100];
//        int resultLength = decompresser.inflate(result);
//        decompresser.end();
//        return result;
//    }
//}
