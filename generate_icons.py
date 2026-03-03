#!/usr/bin/env python3
"""Generate launcher icons for Android project"""
import os
from PIL import Image, ImageDraw

# Icon specifications for different densities
# Format: (density, size_in_dp, base_icon_size, rounded_size)
DENSITIES = {
    'mdpi': (1.0, 48, 48, 48),
    'hdpi': (1.5, 72, 72, 72),
    'xhdpi': (2.0, 96, 96, 96),
    'xxhdpi': (3.0, 144, 144, 144),
    'xxxhdpi': (4.0, 192, 192, 192),
}

def create_launcher_icon(size, filename, is_round=False):
    """Create a simple launcher icon"""
    # Create a new image with a gradient background
    img = Image.new('RGBA', (size, size), (100, 150, 200, 255))
    draw = ImageDraw.Draw(img)

    if is_round:
        # Draw a circle background
        draw.ellipse([0, 0, size-1, size-1], fill=(80, 130, 180, 255))
    else:
        # Fill with solid color
        draw.rectangle([0, 0, size-1, size-1], fill=(100, 150, 200, 255))

    # Draw a simple "S" for StockKeeper in the center
    # Use a simple white circle with letter
    center = size // 2
    radius = size // 3

    # Draw outer circle
    draw.ellipse(
        [center - radius, center - radius, center + radius, center + radius],
        fill=(255, 255, 255, 255)
    )

    # Draw inner letter "S" with a darker color
    text_color = (100, 150, 200, 255)
    try:
        # Try to use a larger font if available
        from PIL import ImageFont
        font_size = max(size // 4, 10)
        font = ImageFont.load_default()
    except:
        font = None

    # Draw "S" text (using simple approach)
    text = "S"
    draw.text((center - size//8, center - size//8), text, fill=text_color, font=font)

    # Save the image
    img.save(filename, 'PNG')
    print(f"Created {filename}")

def main():
    base_path = 'D:\\[L0]WEB\\stock-keeper\\app\\src\\main\\res'

    for density, (scale, size, icon_size, rounded_size) in DENSITIES.items():
        # Regular icon
        regular_path = os.path.join(base_path, f'mipmap-{density}', 'ic_launcher.png')
        create_launcher_icon(icon_size, regular_path, is_round=False)

        # Rounded icon
        rounded_path = os.path.join(base_path, f'mipmap-{density}', 'ic_launcher_round.png')
        create_launcher_icon(rounded_size, rounded_path, is_round=True)

    print("\n✓ All launcher icons created successfully!")

if __name__ == '__main__':
    main()

