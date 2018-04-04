<b>Evolite Engine v0.2.2i</b>

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/75a73f27b1b24a529f02300a8739bb9e)](https://www.codacy.com/app/joshua90123/EvoliteEngine?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=SoraCasus/EvoliteEngine&amp;utm_campaign=Badge_Grade)

A 3D rendering engine I hope to soon implement into a Game Engine
Planned features: 
<br>
<br>
- [ ] Physics
- [ ] Animation
- [ ] Interactive GUI

Todo:
- [x] Rewrite shader system
- [x] Transfer shaders to new system
- [x] Vao/Vbo management
- [ ] Remove Loader class entirely (Kill the beast!)
    - [x] Remove redundant RawModel class
    - [ ] Migrate to abstracted Texture system
        - [x] Static Textures
        - [x] Normal mapping textures
        - [x] Specular light textures
        - [ ] Skybox textures
    - [ ] Implement better file I/O system
- [ ] Scene graph system
    - [ ] Component based rendering
- [ ] Rebuild Particle System (It did not survive VAO / VBO Migration)
- [ ] Merge OBJ File input (normal mapped and standard OBJ's)
- [ ] Reorganize code
- [ ] Logging system
- [ ] Game loop & Time
- [ ] Smooth Shadows

Bugs:
- [ ] Water rendering improperly
    - Most likely a miscalculation in shader
